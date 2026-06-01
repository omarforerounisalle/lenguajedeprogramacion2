using SistemaAcademico.Domain;

namespace SistemaAcademico.Patterns.Builder;

/// <summary>
/// Patrón Builder (creacional).
///
/// Permite construir objetos <see cref="Estudiante"/> paso a paso, evitando
/// constructores telescópicos cuando hay muchos campos opcionales.
///
/// API fluida: cada Con*(...) retorna el mismo builder; <see cref="Build"/>
/// produce la instancia final.
/// </summary>
public class EstudianteBuilder
{
    private readonly Estudiante _e = new();

    public EstudianteBuilder ConId(int id) { _e.Id = id; return this; }
    public EstudianteBuilder ConNombre(string nombre) { _e.Nombre = nombre; return this; }
    public EstudianteBuilder ConApellido(string apellido) { _e.Apellido = apellido; return this; }
    public EstudianteBuilder ConCorreo(string correo) { _e.Correo = correo; return this; }
    public EstudianteBuilder ConPrograma(string programa) { _e.Programa = programa; return this; }
    public EstudianteBuilder ConPromedio(double promedio) { _e.Promedio = promedio; return this; }
    public EstudianteBuilder ConTelefono(string telefono) { _e.Telefono = telefono; return this; }
    public EstudianteBuilder ConDireccion(string direccion) { _e.Direccion = direccion; return this; }
    public EstudianteBuilder Inactivo() { _e.Activo = false; return this; }

    public EstudianteBuilder ConCurso(string codigoCurso)
    {
        _e.Cursos.Add(codigoCurso);
        return this;
    }

    public Estudiante Build()
    {
        if (_e.Id <= 0) throw new InvalidOperationException("El estudiante debe tener un Id positivo.");
        if (string.IsNullOrWhiteSpace(_e.Nombre)) throw new InvalidOperationException("El estudiante debe tener nombre.");
        if (string.IsNullOrWhiteSpace(_e.Programa)) throw new InvalidOperationException("El estudiante debe tener programa.");
        return _e;
    }
}
