using SistemaAcademico.Patterns.Command;
using Xunit;

namespace SistemaAcademico.Tests;

public class CommandTests
{
    [Fact(DisplayName = "Command-01: ejecutar comandos aplica los cambios y los registra en el historial")]
    public void EjecutarRegistraEnHistorial()
    {
        var registro = new RegistroMatriculas();
        var gestor = new GestorMatriculas();

        gestor.Ejecutar(new MatricularCommand(registro, 101, "CS101"));
        gestor.Ejecutar(new MatricularCommand(registro, 101, "CS102"));

        Assert.True(registro.EstaMatriculado(101, "CS101"));
        Assert.True(registro.EstaMatriculado(101, "CS102"));
        Assert.Equal(2, gestor.Historial.Count);
    }

    [Fact(DisplayName = "Command-02: DeshacerUltimo() revierte el último comando ejecutado")]
    public void DeshacerUltimoRevierteElCambio()
    {
        var registro = new RegistroMatriculas();
        var gestor = new GestorMatriculas();
        gestor.Ejecutar(new MatricularCommand(registro, 101, "CS101"));
        gestor.Ejecutar(new MatricularCommand(registro, 101, "CS102"));

        var deshecho = gestor.DeshacerUltimo();

        Assert.True(deshecho);
        Assert.True(registro.EstaMatriculado(101, "CS101"));
        Assert.False(registro.EstaMatriculado(101, "CS102"));
        Assert.Single(gestor.Historial);
    }
}
