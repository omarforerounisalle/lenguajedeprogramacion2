using CrudMvcDao.Model;

namespace CrudMvcDao.View;

/// <summary>
/// Vista del patrón MVC.
///
/// SRP: única responsabilidad — interactuar por consola con el usuario.
/// No conoce ni persistencia ni reglas de negocio.
/// </summary>
public class EstudianteView
{
    public int MostrarMenu()
    {
        Console.WriteLine();
        Console.WriteLine("===== CRUD ESTUDIANTES (MVC + DAO) =====");
        Console.WriteLine("1. Crear estudiante");
        Console.WriteLine("2. Listar estudiantes");
        Console.WriteLine("3. Buscar estudiante por id");
        Console.WriteLine("4. Actualizar estudiante");
        Console.WriteLine("5. Eliminar estudiante");
        Console.WriteLine("0. Salir");
        Console.Write("Opción: ");
        return LeerEntero();
    }

    public Estudiante LeerEstudiante()
    {
        Console.Write("Id: ");
        var id = LeerEntero();
        Console.Write("Nombre: ");
        var nombre = Console.ReadLine() ?? string.Empty;
        Console.Write("Apellido: ");
        var apellido = Console.ReadLine() ?? string.Empty;
        Console.Write("Correo: ");
        var correo = Console.ReadLine() ?? string.Empty;
        Console.Write("Programa: ");
        var programa = Console.ReadLine() ?? string.Empty;
        return new Estudiante(id, nombre, apellido, correo, programa);
    }

    public int LeerId()
    {
        Console.Write("Id: ");
        return LeerEntero();
    }

    public void MostrarEstudiante(Estudiante? estudiante)
    {
        if (estudiante is null)
            MostrarMensaje("No se encontró el estudiante.");
        else
            Console.WriteLine(estudiante);
    }

    public void MostrarEstudiantes(IReadOnlyList<Estudiante> estudiantes)
    {
        if (estudiantes.Count == 0)
        {
            MostrarMensaje("No hay estudiantes registrados.");
            return;
        }
        foreach (var e in estudiantes)
            Console.WriteLine(e);
    }

    public void MostrarMensaje(string mensaje) => Console.WriteLine(mensaje);

    private static int LeerEntero()
    {
        while (true)
        {
            var linea = Console.ReadLine();
            if (int.TryParse(linea, out var n))
                return n;
            Console.Write("Valor inválido, ingrese un número entero: ");
        }
    }
}
