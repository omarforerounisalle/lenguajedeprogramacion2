namespace SistemaAcademico.Patterns.Strategy;

/// <summary>
/// Context del patrón Strategy.
/// Delega el cálculo en la <see cref="IPoliticaCalificacion"/> configurada,
/// permitiendo cambiarla sin alterar el código que la usa (OCP + DIP).
/// </summary>
public class CalculadoraNotaFinal
{
    private IPoliticaCalificacion _politica;

    public CalculadoraNotaFinal(IPoliticaCalificacion politica) => _politica = politica;

    public void CambiarPolitica(IPoliticaCalificacion politica) => _politica = politica;

    public double Calcular(IReadOnlyList<double> notas) => _politica.Calcular(notas);
}
