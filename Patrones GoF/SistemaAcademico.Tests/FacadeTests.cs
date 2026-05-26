using SistemaAcademico.Patterns.Facade;
using Xunit;

namespace SistemaAcademico.Tests;

public class FacadeTests
{
    private static MatriculaFacade NuevaFacade(
        out ServicioRegistro registro,
        out ServicioNotificaciones notificaciones)
    {
        var cupos = new ServicioCupos();
        var pagos = new ServicioPagos();
        registro = new ServicioRegistro();
        notificaciones = new ServicioNotificaciones();
        return new MatriculaFacade(cupos, pagos, registro, notificaciones);
    }

    [Fact(DisplayName = "Facade-01: matrícula exitosa registra la matrícula y notifica al estudiante")]
    public void MatriculaExitosa()
    {
        var facade = NuevaFacade(out var registro, out var notificaciones);

        var ok = facade.Matricular(estudianteId: 101, codigoCurso: "CS101");

        Assert.True(ok);
        Assert.Single(registro.Matriculas);
        Assert.Contains(notificaciones.Enviados, m => m.Contains("Matrícula confirmada"));
    }

    [Fact(DisplayName = "Facade-02: matrícula falla cuando no hay cupos y NO se registra")]
    public void MatriculaSinCupoNoSeRegistra()
    {
        var facade = NuevaFacade(out var registro, out var notificaciones);

        var ok = facade.Matricular(estudianteId: 101, codigoCurso: "IN200");

        Assert.False(ok);
        Assert.Empty(registro.Matriculas);
        Assert.Contains(notificaciones.Enviados, m => m.Contains("sin cupos"));
    }
}
