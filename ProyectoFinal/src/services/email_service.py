"""Servicio de notificación por correo electrónico.

``EmailService`` cumple el criterio 7 del proyecto: notifica las
operaciones de **inserción** y **modificación** de la entidad
``Estudiante`` aplicando el patrón **Decorator**.

En este proyecto la entrega del correo es *simulada* (se imprime en
consola / se acumula en memoria). El punto del ejercicio no es hablar
con un servidor SMTP real, sino demostrar cómo el Decorator agrega
canales encima de la notificación base.
"""
from __future__ import annotations

import logging
from typing import Callable, List

from .notificacion import (
    EmailDecorator,
    INotificacion,
    NotificacionBase,
    NotificacionDecorator,
    SmsDecorator,
    WhatsAppDecorator,
)

_log = logging.getLogger(__name__)

_Sender = Callable[[str, str], None]


def _impresion_consola(asunto: str, cuerpo: str) -> None:
    """Sender por defecto: imprime el correo en consola."""
    print(f"\n=== EmailService ===\nAsunto: {asunto}\n{cuerpo}\n====================\n")


class EmailService:
    """Servicio de notificación con cadena de decoradores configurable.

    Por defecto, la cadena es ``NotificacionBase -> EmailDecorator``,
    es decir: el correo se envía SIEMPRE y la notificación in-app lo
    acompaña. Otros canales (SMS, WhatsApp) pueden añadirse con
    :meth:`agregar_sms` y :meth:`agregar_whatsapp`.

    Parameters
    ----------
    sender : Callable[[str, str], None], opcional
        Función que recibe (asunto, cuerpo). En producción se reemplaza
        por un cliente SMTP; en tests por una función *mock*.
    """

    def __init__(self, sender: _Sender | None = None) -> None:
        self._sender: _Sender = sender or _impresion_consola
        self._cadena: INotificacion = EmailDecorator(NotificacionBase())
        self._historial: List[str] = []

    # ----- Composición de decoradores ----------------------------------
    def agregar_sms(self) -> "EmailService":
        self._cadena = SmsDecorator(self._cadena)
        return self

    def agregar_whatsapp(self) -> "EmailService":
        self._cadena = WhatsAppDecorator(self._cadena)
        return self

    def reiniciar(self) -> "EmailService":
        """Vuelve a la cadena base (NotificacionBase + Email)."""
        self._cadena = EmailDecorator(NotificacionBase())
        return self

    # ----- API pública --------------------------------------------------
    def notificar_creacion(self, nombre_completo: str, correo: str) -> str:
        asunto = "Registro exitoso en Sistema Académico"
        cuerpo = (
            f"Hola {nombre_completo},\n"
            f"Tu cuenta ha sido creada con el correo {correo}."
        )
        return self._enviar(asunto, cuerpo)

    def notificar_actualizacion(self, nombre_completo: str, correo: str) -> str:
        asunto = "Datos actualizados"
        cuerpo = (
            f"Hola {nombre_completo},\n"
            f"Tus datos asociados al correo {correo} fueron actualizados."
        )
        return self._enviar(asunto, cuerpo)

    @property
    def historial(self) -> list[str]:
        """Lista de mensajes enviados (útil para pruebas)."""
        return list(self._historial)

    # ----- Internos -----------------------------------------------------
    def _enviar(self, asunto: str, cuerpo: str) -> str:
        traza = self._cadena.enviar(asunto)
        self._sender(asunto, cuerpo)
        self._historial.append(traza)
        _log.info("Notificación enviada: %s", traza)
        return traza
