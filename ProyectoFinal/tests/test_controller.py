"""Pruebas unitarias del :class:`EstudianteController`.

Cubren el CRUD completo, validación de duplicados y la regla de negocio R1.
Cada test usa un repositorio sobre un archivo JSON temporal para no
ensuciar el entorno y para que los tests sean reproducibles.
"""
import tempfile
import unittest
from pathlib import Path

from src.controller import EstudianteController
from src.exceptions import (
    EstudianteDuplicadoError,
    EstudianteNoEncontradoError,
    ReglaNegocioError,
)
from src.repository import EstudianteRepository
from src.services import EmailService


class _EmailServiceMudo(EmailService):
    """EmailService con sender no-op, para tests rápidos sin imprimir."""

    def __init__(self) -> None:
        super().__init__(sender=lambda asunto, cuerpo: None)


class TestEstudianteController(unittest.TestCase):

    def setUp(self):
        self._tmpdir = tempfile.TemporaryDirectory()
        ruta = Path(self._tmpdir.name) / "estudiantes.json"
        self.repo = EstudianteRepository(ruta)
        self.email = _EmailServiceMudo()
        self.ctrl = EstudianteController(self.repo, self.email)

    def tearDown(self):
        self._tmpdir.cleanup()

    # ----- CRUD básico --------------------------------------------------
    def test_crear_y_listar(self):
        creado = self.ctrl.crear(
            nombre="Ana", apellido="Pérez",
            correo="ana@x.com", programa="Sistemas",
            promedio=4.0,
        )
        self.assertEqual(creado.id_estudiante, 1)
        self.assertEqual(len(self.ctrl.listar()), 1)
        self.assertEqual(len(self.email.historial), 1)

    def test_crear_correo_duplicado_lanza(self):
        self.ctrl.crear(
            nombre="A", apellido="B", correo="dup@x.com",
            programa="Sistemas", promedio=4.0,
        )
        with self.assertRaises(EstudianteDuplicadoError):
            self.ctrl.crear(
                nombre="C", apellido="D", correo="DUP@X.COM",
                programa="Sistemas", promedio=3.5,
            )

    def test_actualizar_envia_notificacion(self):
        est = self.ctrl.crear(
            nombre="A", apellido="B", correo="a@b.com",
            programa="Sistemas", promedio=3.5,
        )
        self.email.historial.clear() if False else None  # historial es propiedad
        actualizado = self.ctrl.actualizar(est.id_estudiante, programa="Industrial")
        self.assertEqual(actualizado.programa, "Industrial")
        # 1 correo por la creación + 1 por la actualización
        self.assertEqual(len(self.email.historial), 2)

    def test_eliminar_quita_del_repositorio(self):
        est = self.ctrl.crear(
            nombre="A", apellido="B", correo="a@b.com",
            programa="Sistemas", promedio=3.5,
        )
        self.ctrl.eliminar(est.id_estudiante)
        self.assertEqual(self.ctrl.listar(), [])
        with self.assertRaises(EstudianteNoEncontradoError):
            self.ctrl.obtener(est.id_estudiante)

    def test_obtener_inexistente_lanza(self):
        with self.assertRaises(EstudianteNoEncontradoError):
            self.ctrl.obtener(999)

    # ----- Regla de negocio R1 -----------------------------------------
    def test_matricular_con_promedio_suficiente(self):
        est = self.ctrl.crear(
            nombre="A", apellido="B", correo="a@b.com",
            programa="Sistemas", promedio=3.2,
        )
        self.ctrl.desmatricular(est.id_estudiante)
        matriculado = self.ctrl.matricular(est.id_estudiante)
        self.assertTrue(matriculado.activo)

    def test_matricular_con_promedio_bajo_lanza_regla_negocio(self):
        est = self.ctrl.crear(
            nombre="A", apellido="B", correo="a@b.com",
            programa="Sistemas", promedio=2.4,
        )
        self.ctrl.desmatricular(est.id_estudiante)
        with self.assertRaises(ReglaNegocioError) as cm:
            self.ctrl.matricular(est.id_estudiante)
        self.assertIn("R1", cm.exception.regla)

    def test_matricular_es_idempotente(self):
        est = self.ctrl.crear(
            nombre="A", apellido="B", correo="a@b.com",
            programa="Sistemas", promedio=4.5,
        )
        # Ya está activo desde la creación.
        resultado = self.ctrl.matricular(est.id_estudiante)
        self.assertTrue(resultado.activo)

    # ----- Persistencia entre instancias --------------------------------
    def test_persistencia_sobrevive_reapertura(self):
        self.ctrl.crear(
            nombre="A", apellido="B", correo="a@b.com",
            programa="Sistemas", promedio=4.0,
        )
        # Nuevo controlador apuntando al mismo archivo.
        repo2 = EstudianteRepository(self.repo._ruta)
        ctrl2 = EstudianteController(repo2, _EmailServiceMudo())
        self.assertEqual(len(ctrl2.listar()), 1)


if __name__ == "__main__":  # pragma: no cover
    unittest.main()
