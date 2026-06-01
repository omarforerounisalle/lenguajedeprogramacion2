using SistemaAcademico.Patterns.FactoryMethod;
using Xunit;

namespace SistemaAcademico.Tests;

public class FactoryMethodTests
{
    [Fact(DisplayName = "FactoryMethod-01: cada Generador concreto produce su Reporte específico")]
    public void CadaGeneradorProduceSuReporte()
    {
        GeneradorReporte calif = new GeneradorCalificaciones();
        GeneradorReporte asist = new GeneradorAsistencia();
        GeneradorReporte fin   = new GeneradorFinanciero();

        Assert.IsType<ReporteCalificaciones>(calif.CrearReporte());
        Assert.IsType<ReporteAsistencia>(asist.CrearReporte());
        Assert.IsType<ReporteFinanciero>(fin.CrearReporte());
    }

    [Fact(DisplayName = "FactoryMethod-02: Procesar() integra el reporte creado por el método factoría")]
    public void ProcesarUsaElReporteFabricado()
    {
        GeneradorReporte gen = new GeneradorCalificaciones();
        var salida = gen.Procesar();
        Assert.Contains("[CALIFICACIONES]", salida);
        Assert.Contains("Reporte de calificaciones generado.", salida);
    }
}
