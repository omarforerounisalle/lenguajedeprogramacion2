namespace SistemaAcademico.Patterns.Facade;

/// <summary>
/// Subsistemas que la Facade va a coordinar.
/// Mantienen su API granular para que sigan siendo utilizables por su cuenta
/// si algún cliente avanzado los necesita.
/// </summary>
public class ServicioCupos
{
    private readonly Dictionary<string, int> _cupos = new()
    {
        ["CS101"] = 30,
        ["CS102"] = 25,
        ["IN200"] = 0
    };

    public bool HayCupo(string codigoCurso) =>
        _cupos.TryGetValue(codigoCurso, out var c) && c > 0;

    public void ReservarCupo(string codigoCurso)
    {
        if (!HayCupo(codigoCurso))
            throw new InvalidOperationException($"Sin cupos en {codigoCurso}");
        _cupos[codigoCurso]--;
    }
}

public class ServicioPagos
{
    private readonly HashSet<int> _estudiantesAlDia = new() { 101, 102, 103 };

    public bool EstaAlDia(int estudianteId) => _estudiantesAlDia.Contains(estudianteId);
}

public class ServicioRegistro
{
    public List<string> Matriculas { get; } = new();

    public void RegistrarMatricula(int estudianteId, string codigoCurso) =>
        Matriculas.Add($"{estudianteId}->{codigoCurso}");
}

public class ServicioNotificaciones
{
    public List<string> Enviados { get; } = new();

    public void Notificar(int estudianteId, string mensaje) =>
        Enviados.Add($"to:{estudianteId} :: {mensaje}");
}
