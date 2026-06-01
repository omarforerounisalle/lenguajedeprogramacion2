namespace SistemaAcademico.Domain;

/// <summary>
/// Entidad de dominio compartida por todos los patrones.
/// Tiene varios campos opcionales — caso ideal para aplicar Builder.
/// </summary>
public class Estudiante
{
    public int Id { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public string Apellido { get; set; } = string.Empty;
    public string Correo { get; set; } = string.Empty;
    public string Programa { get; set; } = string.Empty;
    public double Promedio { get; set; }
    public string? Telefono { get; set; }
    public string? Direccion { get; set; }
    public bool Activo { get; set; } = true;
    public List<string> Cursos { get; set; } = new();

    public override string ToString() =>
        $"Estudiante[{Id}] {Nombre} {Apellido} — {Programa} (promedio {Promedio:F2})";
}
