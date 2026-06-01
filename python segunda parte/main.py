from __future__ import annotations

import sys
from pathlib import Path

# Raíz del proyecto (carpeta que contiene main.py)
_ROOT = Path(__file__).resolve().parent
if str(_ROOT) not in sys.path:
    sys.path.insert(0, str(_ROOT))

from carbon_transport.models import Automovil, Bicicleta, Vehiculo
from carbon_transport.persistence import VehiculoRepository


def _default_data_path() -> Path:
    return _ROOT / "data" / "vehiculos.txt"


def _leer_float(mensaje: str) -> float:
    raw = input(mensaje).strip().replace(",", ".")
    return float(raw)


def _leer_str(mensaje: str) -> str:
    return input(mensaje).strip()


def _menu() -> None:
    repo = VehiculoRepository(_default_data_path())
    vehiculos: list[Vehiculo] = repo.cargar_todos()

    while True:
        print()
        print("1) Agregar bicicleta")
        print("2) Agregar automóvil")
        print("3) Listar vehículos y emisiones")
        print("4) Total emisiones CO2 (kg)")
        print("5) Guardar y salir")
        print("0) Salir sin guardar")
        op = input("Opción: ").strip()

        if op == "1":
            vid = _leer_str("Identificador: ")
            km = _leer_float("Distancia (km): ")
            vehiculos.append(Bicicleta(vid, km))
        elif op == "2":
            vid = _leer_str("Identificador: ")
            km = _leer_float("Distancia (km): ")
            l100 = _leer_float("Consumo (L/100 km): ")
            vehiculos.append(Automovil(vid, km, l100))
        elif op == "3":
            for v in vehiculos:
                print(f"  {v!r} -> {v.emision_co2_kg():.4f} kg CO2")
            if not vehiculos:
                print("  (vacío)")
        elif op == "4":
            total = sum(v.emision_co2_kg() for v in vehiculos)
            print(f"Total: {total:.4f} kg CO2")
        elif op == "5":
            repo.guardar_todos(vehiculos)
            print(f"Guardado en {repo.path}")
            break
        elif op == "0":
            print("Saliendo sin guardar.")
            break
        else:
            print("Opción no válida.")


if __name__ == "__main__":
    _menu()
