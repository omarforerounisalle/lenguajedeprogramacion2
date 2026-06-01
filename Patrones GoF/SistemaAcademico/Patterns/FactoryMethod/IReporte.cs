namespace SistemaAcademico.Patterns.FactoryMethod;

/// <summary>
/// Product del patrón Factory Method.
/// </summary>
public interface IReporte
{
    string Tipo { get; }
    string Generar();
}
