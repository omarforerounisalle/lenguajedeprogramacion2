from __future__ import annotations

import json
from pathlib import Path

from carbon_transport.models.vehiculo import Vehiculo, vehicle_from_dict


class VehiculoRepository:
    """Persistencia en texto: una línea JSON por vehículo (UTF-8)."""

    def __init__(self, path: Path) -> None:
        self._path = path

    @property
    def path(self) -> Path:
        return self._path

    def cargar_todos(self) -> list[Vehiculo]:
        if not self._path.exists():
            return []
        texto = self._path.read_text(encoding="utf-8")
        lineas = [ln.strip() for ln in texto.splitlines() if ln.strip()]
        resultado: list[Vehiculo] = []
        for i, linea in enumerate(lineas, start=1):
            try:
                data = json.loads(linea)
            except json.JSONDecodeError as e:
                raise ValueError(f"línea {i}: JSON inválido") from e
            if not isinstance(data, dict):
                raise ValueError(f"línea {i}: se esperaba un objeto JSON")
            resultado.append(vehicle_from_dict(data))
        return resultado

    def guardar_todos(self, vehiculos: list[Vehiculo]) -> None:
        self._path.parent.mkdir(parents=True, exist_ok=True)
        lineas = [json.dumps(v.to_dict(), ensure_ascii=False) for v in vehiculos]
        self._path.write_text("\n".join(lineas) + ("\n" if lineas else ""), encoding="utf-8")
