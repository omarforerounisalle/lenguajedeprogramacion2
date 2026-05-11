from __future__ import annotations

from typing import Any

from carbon_transport.emission_factors import KG_CO2_POR_LITRO_GASOLINA
from carbon_transport.models.vehiculo import Vehiculo


class Automovil(Vehiculo):
    """Automóvil a gasolina: consumo L/100km y factor CO2 por litro."""

    def __init__(
        self,
        identificador: str,
        distancia_km: float,
        litros_por_100_km: float,
    ) -> None:
        super().__init__(identificador, distancia_km)
        if litros_por_100_km <= 0:
            raise ValueError("litros_por_100_km debe ser positivo")
        self._litros_por_100_km = float(litros_por_100_km)

    @property
    def litros_por_100_km(self) -> float:
        return self._litros_por_100_km

    def tipo(self) -> str:
        return "automovil"

    def emision_co2_kg(self) -> float:
        litros = (self.distancia_km / 100.0) * self.litros_por_100_km
        return litros * KG_CO2_POR_LITRO_GASOLINA

    def to_dict(self) -> dict[str, Any]:
        base = super().to_dict()
        base["litros_por_100_km"] = self.litros_por_100_km
        return base

    @classmethod
    def from_dict(cls, data: dict[str, Any]) -> Automovil:
        return cls(
            identificador=str(data["identificador"]),
            distancia_km=float(data["distancia_km"]),
            litros_por_100_km=float(data["litros_por_100_km"]),
        )
