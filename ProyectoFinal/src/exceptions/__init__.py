"""Excepciones personalizadas del dominio."""
from .dominio import (
    DominioError,
    ValidacionError,
    CorreoInvalidoError,
    PromedioInvalidoError,
    EstudianteNoEncontradoError,
    EstudianteDuplicadoError,
    ReglaNegocioError,
)

__all__ = [
    "DominioError",
    "ValidacionError",
    "CorreoInvalidoError",
    "PromedioInvalidoError",
    "EstudianteNoEncontradoError",
    "EstudianteDuplicadoError",
    "ReglaNegocioError",
]
