namespace SistemaAcademico.Domain;

public class Curso
{
    public string Codigo { get; set; } = string.Empty;
    public string Nombre { get; set; } = string.Empty;
    public int Creditos { get; set; }
    public int CuposDisponibles { get; set; }

    public override string ToString() => $"{Codigo} — {Nombre} ({Creditos} créditos)";
}
