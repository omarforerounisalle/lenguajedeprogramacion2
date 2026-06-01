"""Punto de entrada del Sistema Académico.

Ejecutar desde la raíz del proyecto con:

    python -m src.main
"""
from __future__ import annotations

import logging
from pathlib import Path

from src.controller import EstudianteController
from src.repository import EstudianteRepository
from src.services import EmailService
from src.view import EstudianteView


def _configurar_logging() -> None:
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s [%(levelname)s] %(name)s - %(message)s",
    )


def main() -> None:
    _configurar_logging()

    raiz_proyecto = Path(__file__).resolve().parent.parent
    archivo_datos = raiz_proyecto / "data" / "estudiantes.json"

    repositorio = EstudianteRepository(archivo_datos)
    email_service = EmailService()  # sender por defecto: imprime en consola
    controller = EstudianteController(repositorio, email_service)

    app = EstudianteView(controller)
    app.mainloop()


if __name__ == "__main__":
    main()
