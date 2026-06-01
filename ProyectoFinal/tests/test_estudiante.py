"""Pruebas unitarias para el modelo :class:`Estudiante`."""
import unittest

from src.exceptions import (
    CorreoInvalidoError,
    PromedioInvalidoError,
    ValidacionError,
)
from src.model import Estudiante


def _kwargs_validos(**overrides):
    base = dict(
        id_estudiante=1,
        nombre="Omar",
        apellido="Forero",
        correo="omar@unisalle.edu.co",
        programa="Ingenieria de Sistemas",
        promedio=4.2,
    )
    base.update(overrides)
    return base


class TestEstudiante(unittest.TestCase):
    """Validaciones de dominio de la entidad."""

    def test_creacion_valida(self):
        est = Estudiante(**_kwargs_validos())
        self.assertEqual(est.id_estudiante, 1)
        self.assertTrue(est.activo)

    def test_id_negativo_lanza_validacion(self):
        with self.assertRaises(ValidacionError) as cm:
            Estudiante(**_kwargs_validos(id_estudiante=-1))
        self.assertEqual(cm.exception.campo, "id_estudiante")

    def test_nombre_vacio_lanza_validacion(self):
        with self.assertRaises(ValidacionError):
            Estudiante(**_kwargs_validos(nombre="   "))

    def test_correo_invalido_lanza_excepcion_especifica(self):
        with self.assertRaises(CorreoInvalidoError):
            Estudiante(**_kwargs_validos(correo="no-es-correo"))

    def test_promedio_fuera_de_rango_lanza_excepcion(self):
        with self.assertRaises(PromedioInvalidoError):
            Estudiante(**_kwargs_validos(promedio=5.5))
        with self.assertRaises(PromedioInvalidoError):
            Estudiante(**_kwargs_validos(promedio=-0.1))

    def test_roundtrip_dict(self):
        est = Estudiante(**_kwargs_validos(telefono="3001234567"))
        copia = Estudiante.from_dict(est.to_dict())
        self.assertEqual(est, copia)


if __name__ == "__main__":  # pragma: no cover
    unittest.main()
