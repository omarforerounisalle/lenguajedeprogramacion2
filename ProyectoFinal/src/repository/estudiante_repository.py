"""Persistencia de :class:`Estudiante` sobre archivo JSON.

El repositorio se mantiene **agnóstico** del controlador y de la UI:
expone únicamente operaciones CRUD sobre la entidad y se encarga de
escribir/leer el archivo JSON de forma atómica (escribe a un archivo
temporal y luego lo renombra) para evitar corrupción si el proceso
se interrumpe a mitad de la escritura.
"""
from __future__ import annotations

import json
import os
import tempfile
from pathlib import Path
from typing import Iterable

from src.exceptions import EstudianteNoEncontradoError
from src.model import Estudiante


class EstudianteRepository:
    """Repositorio CRUD persistente en JSON.

    Parameters
    ----------
    ruta_archivo : Path | str
        Ruta al archivo JSON. Si no existe se crea con una lista vacía.
    """

    def __init__(self, ruta_archivo: Path | str) -> None:
        self._ruta = Path(ruta_archivo)
        self._ruta.parent.mkdir(parents=True, exist_ok=True)
        if not self._ruta.exists():
            self._guardar_lista([])

    # ----- CRUD ---------------------------------------------------------
    def crear(self, estudiante: Estudiante) -> Estudiante:
        """Agrega un estudiante. El controlador valida duplicados antes."""
        datos = self._cargar_lista()
        datos.append(estudiante.to_dict())
        self._guardar_lista(datos)
        return estudiante

    def listar(self) -> list[Estudiante]:
        """Devuelve todos los estudiantes persistidos."""
        return [Estudiante.from_dict(d) for d in self._cargar_lista()]

    def obtener(self, id_estudiante: int) -> Estudiante:
        """Devuelve un estudiante por id o lanza ``EstudianteNoEncontradoError``."""
        for d in self._cargar_lista():
            if int(d["id_estudiante"]) == id_estudiante:
                return Estudiante.from_dict(d)
        raise EstudianteNoEncontradoError(id_estudiante)

    def buscar_por_correo(self, correo: str) -> Estudiante | None:
        """Devuelve un estudiante por correo (o ``None`` si no existe)."""
        correo = correo.strip().lower()
        for d in self._cargar_lista():
            if str(d["correo"]).strip().lower() == correo:
                return Estudiante.from_dict(d)
        return None

    def actualizar(self, estudiante: Estudiante) -> Estudiante:
        """Reemplaza el estudiante con el mismo id (lanza si no existe)."""
        datos = self._cargar_lista()
        for i, d in enumerate(datos):
            if int(d["id_estudiante"]) == estudiante.id_estudiante:
                datos[i] = estudiante.to_dict()
                self._guardar_lista(datos)
                return estudiante
        raise EstudianteNoEncontradoError(estudiante.id_estudiante)

    def eliminar(self, id_estudiante: int) -> None:
        """Elimina el estudiante por id (lanza si no existe)."""
        datos = self._cargar_lista()
        nuevos = [d for d in datos if int(d["id_estudiante"]) != id_estudiante]
        if len(nuevos) == len(datos):
            raise EstudianteNoEncontradoError(id_estudiante)
        self._guardar_lista(nuevos)

    def siguiente_id(self) -> int:
        """Genera el siguiente id positivo (max + 1, o 1 si está vacío)."""
        ids = [int(d["id_estudiante"]) for d in self._cargar_lista()]
        return max(ids) + 1 if ids else 1

    # ----- IO interna ---------------------------------------------------
    def _cargar_lista(self) -> list[dict]:
        with self._ruta.open("r", encoding="utf-8") as fh:
            contenido = fh.read().strip()
        if not contenido:
            return []
        datos = json.loads(contenido)
        if not isinstance(datos, list):
            raise ValueError(f"El archivo {self._ruta} no contiene una lista JSON")
        return datos

    def _guardar_lista(self, datos: Iterable[dict]) -> None:
        """Escritura atómica: temp file + os.replace."""
        directorio = self._ruta.parent
        with tempfile.NamedTemporaryFile(
            mode="w",
            encoding="utf-8",
            dir=directorio,
            delete=False,
            suffix=".tmp",
        ) as tmp:
            json.dump(list(datos), tmp, ensure_ascii=False, indent=2)
            ruta_tmp = tmp.name
        os.replace(ruta_tmp, self._ruta)
