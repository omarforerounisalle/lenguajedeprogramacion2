namespace CrudMvcDao.Model;

/// <summary>
/// Modelo del patrón MVC.
/// SRP: representa únicamente los datos de un estudiante; no conoce nada sobre
/// persistencia, vista ni reglas de orquestación.
/// </summary>
public class Estudiante
{
    public int Id { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public string Apellido { get; set; } = string.Empty;
    public string Correo { get; set; } = string.Empty;
    public string Programa { get; set; } = string.Empty;

    public Estudiante() { }

    public Estudiante(int id, string nombre, string apellido, string correo, string programa)
    {
        Id = id;
        Nombre = nombre;
        Apellido = apellido;
        Correo = correo;
        Programa = programa;
    }

    public override string ToString() =>
        $"Estudiante{{Id={Id}, Nombre='{Nombre}', Apellido='{Apellido}', " +
        $"Correo='{Correo}', Programa='{Programa}'}}";

    public override bool Equals(object? obj) =>
        obj is Estudiante e &&
        Id == e.Id && Nombre == e.Nombre && Apellido == e.Apellido &&
        Correo == e.Correo && Programa == e.Programa;

    public override int GetHashCode() => HashCode.Combine(Id, Nombre, Apellido, Correo, Programa);
}
