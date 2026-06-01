namespace SistemaAcademico.Domain;

public class Matricula
{
    public int EstudianteId { get; set; }
    public string CodigoCurso { get; set; } = string.Empty;
    public DateTime Fecha { get; set; } = DateTime.UtcNow;
    public string Estado { get; set; } = "ACTIVA"; // ACTIVA | RETIRADA

    public override string ToString() =>
        $"Matrícula(est={EstudianteId}, curso={CodigoCurso}, estado={Estado})";
}
