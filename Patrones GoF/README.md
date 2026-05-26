# Patrones GoF — Sistema Académico Unisalle

Actividad de **Lenguaje de Programación 2** sobre los 23 patrones de diseño del
*Gang of Four* (GoF), con aplicación práctica de 7 patrones a un sistema
académico de ejemplo.

## Contenido

| Archivo / Carpeta | Descripción |
|-------------------|-------------|
| [`PatronesGoF.md`](PatronesGoF.md) | Documento conceptual: tabla con los 23 patrones + diagramas UML (Mermaid). |
| [`ManualPruebas.md`](ManualPruebas.md) | Manual paso a paso para ejecutar y verificar las 14 pruebas unitarias. |
| [`SistemaAcademico/`](SistemaAcademico/) | Proyecto C# (.NET 8) con los 7 patrones aplicados. |
| [`SistemaAcademico.Tests/`](SistemaAcademico.Tests/) | Proyecto de pruebas con xUnit — 2 pruebas por patrón. |

## Proyecto base — Sistema Académico Unisalle

Mini-sistema universitario que gestiona estudiantes, cursos y matrículas.
Sobre este contexto se aplican los 7 patrones requeridos:

| Patrón | Aplicación en el sistema |
|--------|--------------------------|
| **Builder** | Construcción flexible de un `Estudiante` con muchos campos opcionales. |
| **Factory Method** | Generación de reportes (`Calificaciones`, `Asistencia`, `Financiero`). |
| **Singleton** | `ConfiguracionSistema` única para todo el sistema. |
| **Decorator** | `Notificacion` decorable con canales (Email, SMS, WhatsApp). |
| **Strategy** | `PoliticaCalificacion` intercambiable (promedio simple, ponderado, …). |
| **Facade** | `MatriculaFacade` que orquesta cupos, pagos y notificaciones. |
| **Command** | `AccionMatricula` con undo (matricular/retirar reversibles). |

## Cómo correr las pruebas

Requisitos: .NET 8 SDK.

```bash
cd "Patrones GoF"
dotnet test
```

Salida esperada: `Passed: 14, Failed: 0`.

## Stack

- C# 12 / .NET 8
- xUnit 2.9
- System.Text.Json (sin dependencias externas)
