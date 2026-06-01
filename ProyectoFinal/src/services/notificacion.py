"""Patrón GoF Decorator aplicado a notificaciones.

Estructura clásica:

* ``INotificacion``      — Component.
* ``NotificacionBase``   — ConcreteComponent (notificación in-app).
* ``NotificacionDecorator`` — Decorator abstracto que envuelve a otro
  ``INotificacion`` y delega la operación, dejando que las subclases
  agreguen comportamiento (canal extra) antes o después.
* ``EmailDecorator`` / ``SmsDecorator`` / ``WhatsAppDecorator`` — Concretes.

La idea es que los canales se puedan **componer dinámicamente** en tiempo
de ejecución sin tocar las clases existentes (Open/Closed Principle).
"""
from __future__ import annotations

import logging
from abc import ABC, abstractmethod

_log = logging.getLogger(__name__)


class INotificacion(ABC):
    """Component del patrón Decorator."""

    @abstractmethod
    def enviar(self, mensaje: str) -> str:  # pragma: no cover - interface
        ...


class NotificacionBase(INotificacion):
    """ConcreteComponent: notificación in-app sin canales adicionales."""

    def enviar(self, mensaje: str) -> str:
        return f"[InApp] {mensaje}"


class NotificacionDecorator(INotificacion):
    """Decorator abstracto. Mantiene la referencia al componente envuelto."""

    def __init__(self, wrappee: INotificacion) -> None:
        self._wrappee = wrappee

    @abstractmethod
    def enviar(self, mensaje: str) -> str:  # pragma: no cover - interface
        ...


class EmailDecorator(NotificacionDecorator):
    """Agrega el canal Email a la cadena de notificación."""

    def enviar(self, mensaje: str) -> str:
        salida_previa = self._wrappee.enviar(mensaje)
        _log.info("Email enviado: %s", mensaje)
        return f"{salida_previa} + [Email]"


class SmsDecorator(NotificacionDecorator):
    """Agrega el canal SMS a la cadena de notificación."""

    def enviar(self, mensaje: str) -> str:
        salida_previa = self._wrappee.enviar(mensaje)
        _log.info("SMS enviado: %s", mensaje)
        return f"{salida_previa} + [SMS]"


class WhatsAppDecorator(NotificacionDecorator):
    """Agrega el canal WhatsApp a la cadena de notificación."""

    def enviar(self, mensaje: str) -> str:
        salida_previa = self._wrappee.enviar(mensaje)
        _log.info("WhatsApp enviado: %s", mensaje)
        return f"{salida_previa} + [WhatsApp]"
