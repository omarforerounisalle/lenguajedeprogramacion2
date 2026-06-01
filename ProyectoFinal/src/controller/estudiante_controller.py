"""Controlador del CRUD de Estudiantes.

Coordina la entidad de dominio, el repositorio y el servicio de
notificaciones. Concentra además la **regla de negocio** del proyecto:

    Regla R1 - Matrícula condicionada al promedio
    Solo se puede matricular un estudiante si su promedio >= 3.0.
    Si está por debajo, se lanza :class:`ReglaNegocioError` y la operación
    se aborta sin tocar el repositorio.

La regla está expuesta a través de :meth:`matricular`, que es el método
que aparece en el mapa de procesos BPMN.
"""
from __future__ import annotations

import logging
from typing import Iterable

from src.exceptions import (
    EstudianteDuplicadoError,
    EstudianteNoEncontradoError,
    ReglaNegocioError,
)
from src.model import Estudiante
from src.repository import EstudianteRepository
from src.services import EmailService

_log = logging.getLogger(__name__)

PROMEDIO_MINIMO_MATRICULA = 3.0


class EstudianteController:
    """Orquesta el CRUD + reglas de negocio del Sistema Académico."""

    def __init__(
        self,
        repositorio: EstudianteRepository,
        email_service: EmailService,
    ) -> None:
        self._repositorio = repositorio
        self._email = email_service

    # ----- CRUD ---------------------------------------------------------
    def crear(
        self,
        nombre: str,
        apellido: str,
        correo: str,
        programa: str,
        promedio: float,
        telefono: str | None = None,
        direccion: str | None = None,
    ) -> Estudiante:
        """Crea un nuevo estudiante, valida unicidad de correo y notifica."""
        correo_norm = correo.strip().lower()
        if self._repositorio.buscar_por_correo(correo_norm) is not None:
            raise EstudianteDuplicadoError(correo_norm)

        nuevo = Estudiante(
            id_estudiante=self._repositorio.siguiente_id(),
            nombre=nombre.strip(),
            apellido=apellido.strip(),
            correo=correo_norm,
            programa=programa.strip(),
            promedio=float(promedio),
            telefono=(telefono.strip() if telefono else None),
            direccion=(direccion.strip() if direccion else None),
            activo=True,
        )
        self._repositorio.crear(nuevo)
        self._email.notificar_creacion(f"{nuevo.nombre} {nuevo.apellido}", nuevo.correo)
        _log.info("Estudiante creado: %s", nuevo)
        return nuevo

    def listar(self) -> list[Estudiante]:
        return self._repositorio.listar()

    def obtener(self, id_estudiante: int) -> Estudiante:
        return self._repositorio.obtener(id_estudiante)

    def actualizar(
        self,
        id_estudiante: int,
        *,
        nombre: str | None = None,
        apellido: str | None = None,
        correo: str | None = None,
        programa: str | None = None,
        promedio: float | None = None,
        telefono: str | None = None,
        direccion: str | None = None,
        activo: bool | None = None,
    ) -> Estudiante:
        """Actualiza únicamente los campos no-None del estudiante.

        Si el correo cambia, valida que ningún otro estudiante lo use.
        """
        actual = self._repositorio.obtener(id_estudiante)

        if correo is not None:
            correo_norm = correo.strip().lower()
            otro = self._repositorio.buscar_por_correo(correo_norm)
            if otro is not None and otro.id_estudiante != id_estudiante:
                raise EstudianteDuplicadoError(correo_norm)
        else:
            correo_norm = actual.correo

        nuevo = Estudiante(
            id_estudiante=actual.id_estudiante,
            nombre=(nombre.strip() if nombre is not None else actual.nombre),
            apellido=(apellido.strip() if apellido is not None else actual.apellido),
            correo=correo_norm,
            programa=(programa.strip() if programa is not None else actual.programa),
            promedio=(float(promedio) if promedio is not None else actual.promedio),
            telefono=(
                (telefono.strip() if telefono else None)
                if telefono is not None
                else actual.telefono
            ),
            direccion=(
                (direccion.strip() if direccion else None)
                if direccion is not None
                else actual.direccion
            ),
            activo=(bool(activo) if activo is not None else actual.activo),
        )
        self._repositorio.actualizar(nuevo)
        self._email.notificar_actualizacion(
            f"{nuevo.nombre} {nuevo.apellido}", nuevo.correo
        )
        _log.info("Estudiante actualizado: %s", nuevo)
        return nuevo

    def eliminar(self, id_estudiante: int) -> None:
        """Elimina el estudiante. No envía correo (es una baja)."""
        self._repositorio.eliminar(id_estudiante)
        _log.info("Estudiante eliminado: id=%s", id_estudiante)

    # ----- Regla de negocio --------------------------------------------
    def matricular(self, id_estudiante: int) -> Estudiante:
        """**Regla R1**: solo se matricula si el promedio es >= 3.0.

        Cambia ``activo = True``. Si el estudiante ya está activo,
        la operación es idempotente.
        """
        estudiante = self._repositorio.obtener(id_estudiante)
        if estudiante.promedio < PROMEDIO_MINIMO_MATRICULA:
            raise ReglaNegocioError(
                "R1-MatriculaPromedio",
                (
                    f"El estudiante {estudiante.id_estudiante} tiene promedio "
                    f"{estudiante.promedio:.2f}, inferior al mínimo "
                    f"({PROMEDIO_MINIMO_MATRICULA:.2f}). No puede matricularse."
                ),
            )
        if estudiante.activo:
            return estudiante  # Idempotencia
        actualizado = self.actualizar(id_estudiante, activo=True)
        return actualizado

    def desmatricular(self, id_estudiante: int) -> Estudiante:
        """Inversa de :meth:`matricular` (no aplica la regla de promedio)."""
        return self.actualizar(id_estudiante, activo=False)

    # ----- Filtros utilitarios -----------------------------------------
    def filtrar_por_programa(self, programa: str) -> Iterable[Estudiante]:
        programa = programa.strip().lower()
        return (e for e in self.listar() if e.programa.lower() == programa)
