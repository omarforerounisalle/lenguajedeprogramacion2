using SistemaAcademico.Patterns.Singleton;
using Xunit;

namespace SistemaAcademico.Tests;

public class SingletonTests
{
    [Fact(DisplayName = "Singleton-01: dos referencias apuntan a la misma instancia")]
    public void DosReferenciasMismaInstancia()
    {
        var a = ConfiguracionSistema.Instance;
        var b = ConfiguracionSistema.Instance;

        Assert.Same(a, b);
    }

    [Fact(DisplayName = "Singleton-02: cambios en una referencia se reflejan en cualquier otra")]
    public void CambiosCompartidosEntreReferencias()
    {
        ConfiguracionSistema.Instance.Reset();
        ConfiguracionSistema.Instance.SemestreActual = "2026-2";

        Assert.Equal("2026-2", ConfiguracionSistema.Instance.SemestreActual);

        ConfiguracionSistema.Instance.Reset();
        Assert.Equal("2026-1", ConfiguracionSistema.Instance.SemestreActual);
    }
}
