"""Servicios transversales (EmailService, notificaciones)."""
from .email_service import EmailService
from .notificacion import (
    EmailDecorator,
    INotificacion,
    NotificacionBase,
    NotificacionDecorator,
    SmsDecorator,
    WhatsAppDecorator,
)

__all__ = [
    "EmailService",
    "INotificacion",
    "NotificacionBase",
    "NotificacionDecorator",
    "EmailDecorator",
    "SmsDecorator",
    "WhatsAppDecorator",
]
