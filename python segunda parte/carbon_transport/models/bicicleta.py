from __future__ import annotations

from typing import Any

from carbon_transport.emission_factors import KG_CO2_POR_KM_BICICLETA_USO
from carbon_transport.models.vehiculo import Vehiculo


class Bicicleta(Vehiculo):
    """Medio de transporte sin combustible en uso; emisión de uso ~0."""

    def tipo(self) -> str:
        return "bicicleta"

    def emision_co2_kg(self) -> float:
        return self.distancia_km * KG_CO2_POR_KM_BICICLETA_USO

    def to_dict(self) -> dict[str, Any]:
        base = super().to_dict()
        return base

    @classmethod
    def from_dict(cls, data: dict[str, Any]) -> Bicicleta:
        return cls(
            identificador=str(data["identificador"]),
            distancia_km=float(data["distancia_km"]),
        )
