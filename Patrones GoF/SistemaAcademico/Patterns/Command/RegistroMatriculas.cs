namespace SistemaAcademico.Patterns.Command;

/// <summary>
/// Receiver del patrón Command: sabe cómo realizar las operaciones reales.
/// </summary>
public class RegistroMatriculas
{
    private readonly HashSet<string> _activas = new();

    public IReadOnlyCollection<string> Activas => _activas;

    public bool Matricular(int estudianteId, string codigoCurso) =>
        _activas.Add(Clave(estudianteId, codigoCurso));

    public bool Retirar(int estudianteId, string codigoCurso) =>
        _activas.Remove(Clave(estudianteId, codigoCurso));

    public bool EstaMatriculado(int estudianteId, string codigoCurso) =>
        _activas.Contains(Clave(estudianteId, codigoCurso));

    private static string Clave(int estudianteId, string codigoCurso) =>
        $"{estudianteId}::{codigoCurso}";
}
