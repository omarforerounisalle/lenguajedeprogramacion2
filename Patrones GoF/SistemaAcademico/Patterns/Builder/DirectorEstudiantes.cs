using SistemaAcademico.Domain;

namespace SistemaAcademico.Patterns.Builder;

/// <summary>
/// Director del patrón Builder.
/// Encapsula recetas comunes de construcción reutilizables.
/// </summary>
public class DirectorEstudiantes
{
    public Estudiante CrearEstudianteNuevoIngreso(int id, string nombre, string apellido, string programa) =>
        new EstudianteBuilder()
            .ConId(id)
            .ConNombre(nombre)
            .ConApellido(apellido)
            .ConPrograma(programa)
            .ConCorreo($"{nombre}.{apellido}@unisalle.edu.co".ToLowerInvariant())
            .ConPromedio(0.0)
            .Build();

    public Estudiante CrearEstudianteEgresado(int id, string nombre, string apellido, string programa, double promedio) =>
        new EstudianteBuilder()
            .ConId(id)
            .ConNombre(nombre)
            .ConApellido(apellido)
            .ConPrograma(programa)
            .ConPromedio(promedio)
            .Inactivo()
            .Build();
}
