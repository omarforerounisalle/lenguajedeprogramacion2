import json
import tempfile
import unittest
from pathlib import Path

from carbon_transport.models import Automovil, Bicicleta
from carbon_transport.persistence.repository import VehiculoRepository


class TestVehiculoRepository(unittest.TestCase):
    def test_archivo_inexistente_devuelve_vacio(self) -> None:
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "no_existe.txt"
            repo = VehiculoRepository(path)
            self.assertEqual(repo.cargar_todos(), [])

    def test_guardar_y_cargar_redondo(self) -> None:
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "v.txt"
            repo = VehiculoRepository(path)
            originales = [
                Bicicleta("b1", 12.0),
                Automovil("a1", 30.0, 6.0),
            ]
            repo.guardar_todos(originales)
            cargados = repo.cargar_todos()
            self.assertEqual(len(cargados), 2)
            self.assertEqual(sum(v.emision_co2_kg() for v in cargados), sum(v.emision_co2_kg() for v in originales))
            self.assertIsInstance(cargados[0], Bicicleta)
            self.assertIsInstance(cargados[1], Automovil)

    def test_utf8_caracteres(self) -> None:
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "v.txt"
            repo = VehiculoRepository(path)
            repo.guardar_todos([Bicicleta("ñoño", 1.0)])
            linea = path.read_text(encoding="utf-8").strip()
            data = json.loads(linea)
            self.assertEqual(data["identificador"], "ñoño")

    def test_json_invalido_lanza(self) -> None:
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "v.txt"
            path.write_text("{no json\n", encoding="utf-8")
            repo = VehiculoRepository(path)
            with self.assertRaises(ValueError):
                repo.cargar_todos()


if __name__ == "__main__":
    unittest.main()
