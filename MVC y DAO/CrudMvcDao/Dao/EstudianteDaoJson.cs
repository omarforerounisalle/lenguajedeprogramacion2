using System.Text.Json;
using CrudMvcDao.Model;

namespace CrudMvcDao.Dao;

/// <summary>
/// Implementación del DAO que persiste estudiantes en un archivo JSON.
///
/// SRP: única responsabilidad — leer y escribir estudiantes en JSON.
/// LSP: respeta el contrato declarado por <see cref="IEstudianteDao"/>;
/// cualquier implementación puede sustituirla sin romper a los clientes.
/// </summary>
public class EstudianteDaoJson : IEstudianteDao
{
    private readonly string _archivoPath;
    private readonly JsonSerializerOptions _options;

    public EstudianteDaoJson(string archivoPath)
    {
        _archivoPath = archivoPath;
        _options = new JsonSerializerOptions { WriteIndented = true };
        InicializarArchivoSiNoExiste();
    }

    private void InicializarArchivoSiNoExiste()
    {
        if (!File.Exists(_archivoPath))
        {
            var dir = Path.GetDirectoryName(_archivoPath);
            if (!string.IsNullOrEmpty(dir) && !Directory.Exists(dir))
                Directory.CreateDirectory(dir);
            EscribirTodos(new List<Estudiante>());
        }
    }

    public Estudiante Crear(Estudiante estudiante)
    {
        ArgumentNullException.ThrowIfNull(estudiante);
        var estudiantes = LeerTodos();
        if (estudiantes.Any(e => e.Id == estudiante.Id))
            throw new ArgumentException($"Ya existe un estudiante con id {estudiante.Id}");
        estudiantes.Add(estudiante);
        EscribirTodos(estudiantes);
        return estudiante;
    }

    public IReadOnlyList<Estudiante> Listar() => LeerTodos();

    public Estudiante? BuscarPorId(int id) =>
        LeerTodos().FirstOrDefault(e => e.Id == id);

    public Estudiante Actualizar(Estudiante estudiante)
    {
        ArgumentNullException.ThrowIfNull(estudiante);
        var estudiantes = LeerTodos();
        var idx = estudiantes.FindIndex(e => e.Id == estudiante.Id);
        if (idx < 0)
            throw new ArgumentException($"No existe un estudiante con id {estudiante.Id}");
        estudiantes[idx] = estudiante;
        EscribirTodos(estudiantes);
        return estudiante;
    }

    public bool Eliminar(int id)
    {
        var estudiantes = LeerTodos();
        var removidos = estudiantes.RemoveAll(e => e.Id == id);
        if (removidos > 0)
            EscribirTodos(estudiantes);
        return removidos > 0;
    }

    private List<Estudiante> LeerTodos()
    {
        if (!File.Exists(_archivoPath) || new FileInfo(_archivoPath).Length == 0)
            return new List<Estudiante>();
        var json = File.ReadAllText(_archivoPath);
        if (string.IsNullOrWhiteSpace(json))
            return new List<Estudiante>();
        return JsonSerializer.Deserialize<List<Estudiante>>(json, _options)
               ?? new List<Estudiante>();
    }

    private void EscribirTodos(List<Estudiante> estudiantes)
    {
        var json = JsonSerializer.Serialize(estudiantes, _options);
        File.WriteAllText(_archivoPath, json);
    }
}
