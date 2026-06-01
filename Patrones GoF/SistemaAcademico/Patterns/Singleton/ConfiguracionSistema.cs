namespace SistemaAcademico.Patterns.Singleton;

/// <summary>
/// Patrón Singleton (creacional).
///
/// Garantiza una única instancia de la configuración del sistema en toda la
/// aplicación, accesible a través de <see cref="Instance"/>. Implementación
/// thread-safe usando <see cref="Lazy{T}"/>.
/// </summary>
public sealed class ConfiguracionSistema
{
    private static readonly Lazy<ConfiguracionSistema> _instance =
        new(() => new ConfiguracionSistema());

    public static ConfiguracionSistema Instance => _instance.Value;

    public string Institucion { get; set; } = "Universidad de La Salle";
    public string SemestreActual { get; set; } = "2026-1";
    public string CorreoSoporte { get; set; } = "soporte@unisalle.edu.co";
    public int MaxCreditosPorEstudiante { get; set; } = 18;

    private ConfiguracionSistema()
    {
        // Constructor privado: nadie puede crear instancias adicionales.
    }

    /// <summary>Solo para pruebas: reinicia los valores por defecto.</summary>
    public void Reset()
    {
        Institucion = "Universidad de La Salle";
        SemestreActual = "2026-1";
        CorreoSoporte = "soporte@unisalle.edu.co";
        MaxCreditosPorEstudiante = 18;
    }
}
