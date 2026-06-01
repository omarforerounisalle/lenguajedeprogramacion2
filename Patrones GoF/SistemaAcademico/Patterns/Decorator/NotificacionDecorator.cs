namespace SistemaAcademico.Patterns.Decorator;

/// <summary>
/// Decorator abstracto: mantiene la referencia al componente envuelto y
/// delega la operación, permitiendo que las subclases agreguen comportamiento
/// antes y/o después.
/// </summary>
public abstract class NotificacionDecorator : INotificacion
{
    protected readonly INotificacion Wrappee;

    protected NotificacionDecorator(INotificacion wrappee)
    {
        Wrappee = wrappee;
    }

    public abstract string Enviar(string mensaje);
}

public class EmailDecorator : NotificacionDecorator
{
    public EmailDecorator(INotificacion wrappee) : base(wrappee) { }
    public override string Enviar(string mensaje) => Wrappee.Enviar(mensaje) + " + [Email]";
}

public class SmsDecorator : NotificacionDecorator
{
    public SmsDecorator(INotificacion wrappee) : base(wrappee) { }
    public override string Enviar(string mensaje) => Wrappee.Enviar(mensaje) + " + [SMS]";
}

public class WhatsAppDecorator : NotificacionDecorator
{
    public WhatsAppDecorator(INotificacion wrappee) : base(wrappee) { }
    public override string Enviar(string mensaje) => Wrappee.Enviar(mensaje) + " + [WhatsApp]";
}
