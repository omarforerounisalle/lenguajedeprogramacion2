namespace SistemaAcademico.Patterns.FactoryMethod;

/// <summary>
/// Creator abstracto del patrón Factory Method.
/// Declara el método factoría que las subclases concretas implementan
/// para crear su variante de <see cref="IReporte"/>.
/// </summary>
public abstract class GeneradorReporte
{
    public abstract IReporte CrearReporte();

    /// <summary>Lógica de negocio común que delega la creación en el método factoría.</summary>
    public string Procesar()
    {
        var reporte = CrearReporte();
        return $"[{reporte.Tipo}] {reporte.Generar()}";
    }
}

public class GeneradorCalificaciones : GeneradorReporte
{
    public override IReporte CrearReporte() => new ReporteCalificaciones();
}

public class GeneradorAsistencia : GeneradorReporte
{
    public override IReporte CrearReporte() => new ReporteAsistencia();
}

public class GeneradorFinanciero : GeneradorReporte
{
    public override IReporte CrearReporte() => new ReporteFinanciero();
}
