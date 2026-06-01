namespace SistemaAcademico.Patterns.Strategy;

/// <summary>
/// Strategy: contrato para calcular la nota final a partir de varias notas
/// parciales. Las distintas políticas son intercambiables en tiempo de
/// ejecución sin tocar al contexto.
/// </summary>
public interface IPoliticaCalificacion
{
    double Calcular(IReadOnlyList<double> notas);
}

public class PromedioSimple : IPoliticaCalificacion
{
    public double Calcular(IReadOnlyList<double> notas)
    {
        if (notas.Count == 0) return 0;
        return notas.Average();
    }
}

public class PromedioPonderado : IPoliticaCalificacion
{
    private readonly IReadOnlyList<double> _pesos;

    public PromedioPonderado(IReadOnlyList<double> pesos) => _pesos = pesos;

    public double Calcular(IReadOnlyList<double> notas)
    {
        if (notas.Count != _pesos.Count)
            throw new ArgumentException("La cantidad de notas y pesos debe coincidir.");
        return notas.Zip(_pesos, (n, p) => n * p).Sum();
    }
}

public class MejorDeTres : IPoliticaCalificacion
{
    public double Calcular(IReadOnlyList<double> notas)
    {
        if (notas.Count == 0) return 0;
        return notas.OrderByDescending(n => n).Take(3).Average();
    }
}
