using SistemaAcademico.Patterns.Builder;
using Xunit;

namespace SistemaAcademico.Tests;

public class BuilderTests
{
    [Fact(DisplayName = "Builder-01: construcción fluida con todos los campos opcionales")]
    public void BuilderConstruyeConTodosLosCampos()
    {
        var estudiante = new EstudianteBuilder()
            .ConId(101)
            .ConNombre("Ana")
            .ConApellido("Pérez")
            .ConPrograma("Ing. Sistemas")
            .ConCorreo("ana@unisalle.edu.co")
            .ConPromedio(4.3)
            .ConTelefono("3001234567")
            .ConDireccion("Cra 1 #1-01")
            .ConCurso("CS101")
            .ConCurso("CS102")
            .Build();

        Assert.Equal(101, estudiante.Id);
        Assert.Equal("Ana", estudiante.Nombre);
        Assert.Equal(4.3, estudiante.Promedio);
        Assert.True(estudiante.Activo);
        Assert.Equal(2, estudiante.Cursos.Count);
    }

    [Fact(DisplayName = "Builder-02: Director crea estudiante de nuevo ingreso con valores por defecto")]
    public void DirectorCreaEstudianteNuevoIngreso()
    {
        var director = new DirectorEstudiantes();

        var estudiante = director.CrearEstudianteNuevoIngreso(202, "Luis", "Gómez", "Ing. Industrial");

        Assert.Equal(202, estudiante.Id);
        Assert.Equal("luis.gómez@unisalle.edu.co", estudiante.Correo);
        Assert.Equal(0.0, estudiante.Promedio);
        Assert.True(estudiante.Activo);
        Assert.Empty(estudiante.Cursos);
    }
}
