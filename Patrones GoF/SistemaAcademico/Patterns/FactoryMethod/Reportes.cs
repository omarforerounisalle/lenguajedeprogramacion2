namespace SistemaAcademico.Patterns.FactoryMethod;

public class ReporteCalificaciones : IReporte
{
    public string Tipo => "CALIFICACIONES";
    public string Generar() => "Reporte de calificaciones generado.";
}

public class ReporteAsistencia : IReporte
{
    public string Tipo => "ASISTENCIA";
    public string Generar() => "Reporte de asistencia generado.";
}

public class ReporteFinanciero : IReporte
{
    public string Tipo => "FINANCIERO";
    public string Generar() => "Reporte financiero generado.";
}
