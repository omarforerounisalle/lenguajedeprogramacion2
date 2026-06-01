"""Entidad de dominio :class:`Estudiante` con validaciones invariantes.

La entidad se valida en el momento de su construcción y en cada asignación
relevante. Cualquier valor inválido lanza una excepción del módulo
:mod:`src.exceptions`.
"""
from __future__ import annotations

import re
from dataclasses import dataclass, field, asdict
from typing import Any

from src.exceptions import (
    ValidacionError,
    CorreoInvalidoError,
    PromedioInvalidoError,
)

_REGEX_CORREO = re.compile(r"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$")
_PROMEDIO_MIN, _PROMEDIO_MAX = 0.0, 5.0


@dataclass
class Estudiante:
    """Estudiante del sistema académico.

    Atributos
    ---------
    id_estudiante : int
        Identificador único positivo.
    nombre, apellido : str
        Nombres/apellidos no vacíos.
    correo : str
        Correo válido (validado con expresión regular).
    programa : str
        Programa académico al que pertenece (no vacío).
    promedio : float
        Promedio en [0.0, 5.0].
    telefono, direccion : str | None
        Datos de contacto opcionales.
    activo : bool
        Estado del estudiante. Por defecto ``True``.
    """

    id_estudiante: int
    nombre: str
    apellido: str
    correo: str
    programa: str
    promedio: float
    telefono: str | None = None
    direccion: str | None = None
    activo: bool = True

    def __post_init__(self) -> None:
        self._validar()

    # ----- Validaciones -------------------------------------------------
    def _validar(self) -> None:
        if not isinstance(self.id_estudiante, int) or self.id_estudiante <= 0:
            raise ValidacionError("id_estudiante", "Debe ser un entero positivo")

        for campo in ("nombre", "apellido", "programa"):
            valor = getattr(self, campo)
            if not isinstance(valor, str) or not valor.strip():
                raise ValidacionError(campo, "No puede estar vacío")

        if not isinstance(self.correo, str) or not _REGEX_CORREO.match(self.correo):
            raise CorreoInvalidoError(self.correo)

        if not isinstance(self.promedio, (int, float)):
            raise PromedioInvalidoError(self.promedio)
        if not (_PROMEDIO_MIN <= float(self.promedio) <= _PROMEDIO_MAX):
            raise PromedioInvalidoError(float(self.promedio))

        if self.telefono is not None:
            if not isinstance(self.telefono, str) or not self.telefono.strip():
                raise ValidacionError("telefono", "Si se proporciona, no puede ser vacío")

    # ----- Conversión a / desde diccionario (persistencia JSON) ---------
    def to_dict(self) -> dict[str, Any]:
        """Serializa la entidad a un diccionario JSON-friendly."""
        return asdict(self)

    @classmethod
    def from_dict(cls, datos: dict[str, Any]) -> "Estudiante":
        """Reconstruye una entidad desde un diccionario (lanza ValidacionError si inválido)."""
        return cls(
            id_estudiante=int(datos["id_estudiante"]),
            nombre=str(datos["nombre"]).strip(),
            apellido=str(datos["apellido"]).strip(),
            correo=str(datos["correo"]).strip().lower(),
            programa=str(datos["programa"]).strip(),
            promedio=float(datos["promedio"]),
            telefono=(datos.get("telefono") or None),
            direccion=(datos.get("direccion") or None),
            activo=bool(datos.get("activo", True)),
        )

    # ----- Representación legible --------------------------------------
    def __str__(self) -> str:  # pragma: no cover - trivial
        estado = "activo" if self.activo else "inactivo"
        return (
            f"Estudiante[{self.id_estudiante}] {self.nombre} {self.apellido} "
            f"— {self.programa} (promedio {self.promedio:.2f}, {estado})"
        )
