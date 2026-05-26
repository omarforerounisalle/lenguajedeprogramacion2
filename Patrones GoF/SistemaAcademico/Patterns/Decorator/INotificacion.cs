namespace SistemaAcademico.Patterns.Decorator;

/// <summary>
/// Component del patrón Decorator.
/// </summary>
public interface INotificacion
{
    string Enviar(string mensaje);
}

/// <summary>
/// ConcreteComponent: notificación básica (in-app) sin canales adicionales.
/// </summary>
public class NotificacionBase : INotificacion
{
    public string Enviar(string mensaje) => $"[InApp] {mensaje}";
}
