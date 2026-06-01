from __future__ import annotations

from abc import ABC, abstractmethod
from typing import Any


class Vehiculo(ABC):
    """Clase base: datos comunes y contrato polimórfico para emisión CO2."""

    def __init__(self, identificador: str, distancia_km: float) -> None:
        if not identificador or not identificador.strip():
            raise ValueError("identificador no puede estar vacío")
        if distancia_km < 0:
            raise ValueError("distancia_km no puede ser negativa")
        self._id = identificador.strip()
        self._distancia_km = float(distancia_km)

    @property
    def identificador(self) -> str:
        return self._id

    @property
    def distancia_km(self) -> float:
        return self._distancia_km

    @abstractmethod
    def emision_co2_kg(self) -> float:
        """Emisión estimada en kg CO2 para el trayecto dado."""

    @abstractmethod
    def tipo(self) -> str:
        """Etiqueta persistida en archivo."""

    def to_dict(self) -> dict[str, Any]:
        return {
            "tipo": self.tipo(),
            "identificador": self.identificador,
            "distancia_km": self.distancia_km,
        }

    def __repr__(self) -> str:
        return f"{self.__class__.__name__}(id={self.identificador!r}, km={self.distancia_km})"


def vehicle_from_dict(data: dict[str, Any]) -> Vehiculo:
    """Fábrica: reconstruye subclases desde diccionario (una línea JSON)."""
    tipo = data.get("tipo")
    if tipo == "bicicleta":
        from carbon_transport.models.bicicleta import Bicicleta

        return Bicicleta.from_dict(data)
    if tipo == "automovil":
        from carbon_transport.models.automovil import Automovil

        return Automovil.from_dict(data)
    raise ValueError(f"tipo de vehículo desconocido: {tipo!r}")
