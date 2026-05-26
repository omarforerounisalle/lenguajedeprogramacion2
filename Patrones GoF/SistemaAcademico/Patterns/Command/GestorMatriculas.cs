namespace SistemaAcademico.Patterns.Command;

/// <summary>
/// Invoker del patrón Command: ejecuta los comandos y guarda un historial
/// para permitir deshacer la última operación.
/// </summary>
public class GestorMatriculas
{
    private readonly Stack<IAccionMatricula> _historial = new();

    public IReadOnlyCollection<string> Historial =>
        _historial.Select(c => c.Descripcion).ToList();

    public void Ejecutar(IAccionMatricula accion)
    {
        accion.Ejecutar();
        _historial.Push(accion);
    }

    public bool DeshacerUltimo()
    {
        if (_historial.Count == 0) return false;
        var ultimo = _historial.Pop();
        ultimo.Deshacer();
        return true;
    }
}
