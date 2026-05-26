using SistemaAcademico.Patterns.Strategy;
using Xunit;

namespace SistemaAcademico.Tests;

public class StrategyTests
{
    [Fact(DisplayName = "Strategy-01: el promedio ponderado aplica los pesos definidos")]
    public void PromedioPonderadoAplicaPesos()
    {
        var pesos = new[] { 0.3, 0.3, 0.4 };
        var calc = new CalculadoraNotaFinal(new PromedioPonderado(pesos));

        var resultado = calc.Calcular(new[] { 4.0, 3.0, 5.0 });

        // 4.0*0.3 + 3.0*0.3 + 5.0*0.4 = 1.2 + 0.9 + 2.0 = 4.1
        Assert.Equal(4.1, resultado, precision: 2);
    }

    [Fact(DisplayName = "Strategy-02: cambiar la política en tiempo de ejecución cambia el resultado")]
    public void CambiarPoliticaEnTiempoDeEjecucion()
    {
        var notas = new[] { 5.0, 5.0, 4.0, 2.0, 3.0 };
        var calc = new CalculadoraNotaFinal(new PromedioSimple());

        var simple = calc.Calcular(notas);             // (5+5+4+2+3)/5 = 3.8
        calc.CambiarPolitica(new MejorDeTres());
        var mejor = calc.Calcular(notas);              // (5+5+4)/3 = 4.6666...

        Assert.Equal(3.8, simple, precision: 2);
        Assert.Equal(4.67, mejor, precision: 2);
    }
}
