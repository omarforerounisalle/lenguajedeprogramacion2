"""Excepciones personalizadas del dominio del Sistema Académico.

Todas heredan de :class:`DominioError` para permitir capturarlas de forma
genérica desde la capa de presentación y mostrar mensajes amigables al
usuario sin perder el detalle técnico subyacente.
"""
from __future__ import annotations


class DominioError(Exception):
    """Raíz de la jerarquía de errores de dominio."""


class ValidacionError(DominioError):
    """Se lanza cuando un dato no cumple una restricción del dominio."""

    def __init__(self, campo: str, mensaje: str) -> None:
        super().__init__(f"[{campo}] {mensaje}")
        self.campo = campo
        self.mensaje = mensaje


class CorreoInvalidoError(ValidacionError):
    """El correo electrónico no respeta el formato `usuario@dominio.tld`."""

    def __init__(self, correo: str) -> None:
        super().__init__("correo", f"Correo inválido: '{correo}'")
        self.correo = correo


class PromedioInvalidoError(ValidacionError):
    """El promedio está fuera del rango permitido [0.0, 5.0]."""

    def __init__(self, promedio: float) -> None:
        super().__init__("promedio", f"El promedio {promedio} no está en [0.0, 5.0]")
        self.promedio = promedio


class EstudianteNoEncontradoError(DominioError):
    """No existe un estudiante con el identificador solicitado."""

    def __init__(self, id_estudiante: int) -> None:
        super().__init__(f"No existe un estudiante con id={id_estudiante}")
        self.id_estudiante = id_estudiante


class EstudianteDuplicadoError(DominioError):
    """Intento de registrar un correo que ya existe en el repositorio."""

    def __init__(self, correo: str) -> None:
        super().__init__(f"Ya existe un estudiante con el correo '{correo}'")
        self.correo = correo


class ReglaNegocioError(DominioError):
    """Una operación viola una regla de negocio (p. ej. matrícula con promedio bajo)."""

    def __init__(self, regla: str, detalle: str) -> None:
        super().__init__(f"[{regla}] {detalle}")
        self.regla = regla
        self.detalle = detalle
