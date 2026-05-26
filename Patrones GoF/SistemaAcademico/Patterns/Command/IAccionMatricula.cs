namespace SistemaAcademico.Patterns.Command;

/// <summary>
/// Command: encapsula una operación sobre el registro de matrículas como un
/// objeto, permitiendo registrarla en una cola/historial y deshacerla.
/// </summary>
public interface IAccionMatricula
{
    void Ejecutar();
    void Deshacer();
    string Descripcion { get; }
}
