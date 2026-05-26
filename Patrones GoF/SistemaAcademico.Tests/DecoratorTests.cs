using SistemaAcademico.Patterns.Decorator;
using Xunit;

namespace SistemaAcademico.Tests;

public class DecoratorTests
{
    [Fact(DisplayName = "Decorator-01: notificación base sin decoradores solo usa InApp")]
    public void NotificacionBaseSinDecoradores()
    {
        INotificacion n = new NotificacionBase();
        var salida = n.Enviar("Bienvenido");

        Assert.Equal("[InApp] Bienvenido", salida);
    }

    [Fact(DisplayName = "Decorator-02: apilar Email + SMS + WhatsApp añade canales en orden")]
    public void DecoradoresApiladosAgreganCanalesEnOrden()
    {
        INotificacion n = new NotificacionBase();
        n = new EmailDecorator(n);
        n = new SmsDecorator(n);
        n = new WhatsAppDecorator(n);

        var salida = n.Enviar("Matrícula confirmada");

        Assert.Equal("[InApp] Matrícula confirmada + [Email] + [SMS] + [WhatsApp]", salida);
    }
}
