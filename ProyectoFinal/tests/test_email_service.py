"""Pruebas unitarias del :class:`EmailService` y de los decoradores GoF."""
import unittest

from src.services import (
    EmailDecorator,
    EmailService,
    NotificacionBase,
    SmsDecorator,
    WhatsAppDecorator,
)


class TestNotificacionDecorator(unittest.TestCase):
    """Verifica la composición del patrón Decorator."""

    def test_base_sin_decoradores(self):
        salida = NotificacionBase().enviar("hola")
        self.assertEqual(salida, "[InApp] hola")

    def test_email_envuelve_base(self):
        cadena = EmailDecorator(NotificacionBase())
        self.assertEqual(cadena.enviar("hola"), "[InApp] hola + [Email]")

    def test_cadena_de_tres_decoradores(self):
        cadena = WhatsAppDecorator(SmsDecorator(EmailDecorator(NotificacionBase())))
        self.assertEqual(
            cadena.enviar("hola"),
            "[InApp] hola + [Email] + [SMS] + [WhatsApp]",
        )


class TestEmailService(unittest.TestCase):
    """Verifica el servicio que envía notificaciones de inserción/modificación."""

    def setUp(self):
        self.correos_enviados: list[tuple[str, str]] = []

        def sender(asunto, cuerpo):
            self.correos_enviados.append((asunto, cuerpo))

        self.service = EmailService(sender=sender)

    def test_notificar_creacion_envia_correo_y_traza(self):
        traza = self.service.notificar_creacion("Omar Forero", "omar@x.com")
        self.assertEqual(len(self.correos_enviados), 1)
        self.assertIn("Registro exitoso", self.correos_enviados[0][0])
        self.assertIn("[Email]", traza)

    def test_agregar_sms_incluye_canal_en_la_traza(self):
        self.service.agregar_sms()
        traza = self.service.notificar_actualizacion("Omar F.", "omar@x.com")
        self.assertIn("[Email]", traza)
        self.assertIn("[SMS]", traza)

    def test_historial_se_acumula(self):
        self.service.notificar_creacion("A B", "a@b.com")
        self.service.notificar_actualizacion("A B", "a@b.com")
        self.assertEqual(len(self.service.historial), 2)


if __name__ == "__main__":  # pragma: no cover
    unittest.main()
