import unittest

from carbon_transport.emission_factors import KG_CO2_POR_LITRO_GASOLINA
from carbon_transport.models import Automovil, Bicicleta, vehicle_from_dict
from carbon_transport.models.vehiculo import Vehiculo


class TestBicicleta(unittest.TestCase):
    def test_emision_uso_cero(self) -> None:
        b = Bicicleta("B1", 25.0)
        self.assertEqual(b.emision_co2_kg(), 0.0)
        self.assertEqual(b.tipo(), "bicicleta")


class TestAutomovil(unittest.TestCase):
    def test_emision_formula(self) -> None:
        # 100 km, 10 L/100km -> 10 L
        a = Automovil("A1", 100.0, 10.0)
        esperado = 10.0 * KG_CO2_POR_LITRO_GASOLINA
        self.assertAlmostEqual(a.emision_co2_kg(), esperado, places=6)

    def test_validacion_litros(self) -> None:
        with self.assertRaises(ValueError):
            Automovil("A2", 10.0, 0.0)


class TestVehiculoBase(unittest.TestCase):
    def test_validacion_distancia_negativa(self) -> None:
        with self.assertRaises(ValueError):
            Bicicleta("X", -1.0)

    def test_validacion_id_vacio(self) -> None:
        with self.assertRaises(ValueError):
            Bicicleta("  ", 1.0)


class TestPolimorfismo(unittest.TestCase):
    def test_lista_sin_isinstance_para_total(self) -> None:
        items: list[Vehiculo] = [
            Bicicleta("b", 10.0),
            Automovil("a", 50.0, 8.0),
        ]
        total = sum(v.emision_co2_kg() for v in items)
        auto_part = (50.0 / 100.0) * 8.0 * KG_CO2_POR_LITRO_GASOLINA
        self.assertAlmostEqual(total, auto_part, places=6)


class TestFactory(unittest.TestCase):
    def test_roundtrip_bicicleta(self) -> None:
        b = Bicicleta("B9", 3.5)
        d = b.to_dict()
        v = vehicle_from_dict(d)
        self.assertIsInstance(v, Bicicleta)
        self.assertEqual(v.identificador, "B9")
        self.assertEqual(v.distancia_km, 3.5)

    def test_roundtrip_automovil(self) -> None:
        a = Automovil("Z", 20.0, 7.5)
        v = vehicle_from_dict(a.to_dict())
        self.assertIsInstance(v, Automovil)
        self.assertAlmostEqual(v.litros_por_100_km, 7.5)

    def test_tipo_desconocido(self) -> None:
        with self.assertRaises(ValueError):
            vehicle_from_dict({"tipo": "tren", "identificador": "T", "distancia_km": 1})


if __name__ == "__main__":
    unittest.main()
