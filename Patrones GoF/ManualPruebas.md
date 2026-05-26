# Manual de pruebas unitarias — Patrones GoF

Manual paso a paso para ejecutar y verificar las **14 pruebas unitarias**
asociadas a los 7 patrones GoF aplicados en `SistemaAcademico/`.

## 1. Requisitos

| Software        | Versión mínima | Verificación                         |
|-----------------|----------------|--------------------------------------|
| .NET SDK        | 8.0            | `dotnet --version` → `8.0.x`         |
| Git             | 2.x            | `git --version`                      |
| Sistema operativo | Windows, macOS o Linux | —                            |

Si no tiene .NET 8 SDK:

- **Windows**: `winget install --id Microsoft.DotNet.SDK.8`
- **macOS / Linux**: https://dotnet.microsoft.com/download/dotnet/8.0
- Reinicie su terminal para que `dotnet` aparezca en el PATH.

## 2. Obtener el código

```bash
git clone https://github.com/omarforerounisalle/lenguajedeprogramacion2.git
cd lenguajedeprogramacion2
git checkout development
cd "Patrones GoF"
```

## 3. Restaurar dependencias y compilar

```bash
dotnet restore
dotnet build
```

Salida esperada (resumen):

```
Build succeeded.
    0 Warning(s)
    0 Error(s)
```

## 4. Ejecutar las 14 pruebas

### 4.1 Todas a la vez

```bash
dotnet test
```

Salida esperada:

```
Passed!  - Failed: 0, Passed: 14, Skipped: 0, Total: 14, Duration: ~30 ms
```

### 4.2 Salida detallada (recomendado para el pantallazo)

```bash
dotnet test --logger "console;verbosity=detailed"
```

Imprime el nombre y resultado de cada una de las 14 pruebas:

```
Passed Builder-01: construcción fluida con todos los campos opcionales
Passed Builder-02: Director crea estudiante de nuevo ingreso con valores por defecto
Passed FactoryMethod-01: cada Generador concreto produce su Reporte específico
Passed FactoryMethod-02: Procesar() integra el reporte creado por el método factoría
Passed Singleton-01: dos referencias apuntan a la misma instancia
Passed Singleton-02: cambios en una referencia se reflejan en cualquier otra
Passed Decorator-01: notificación base sin decoradores solo usa InApp
Passed Decorator-02: apilar Email + SMS + WhatsApp añade canales en orden
Passed Strategy-01: el promedio ponderado aplica los pesos definidos
Passed Strategy-02: cambiar la política en tiempo de ejecución cambia el resultado
Passed Facade-01: matrícula exitosa registra la matrícula y notifica al estudiante
Passed Facade-02: matrícula falla cuando no hay cupos y NO se registra
Passed Command-01: ejecutar comandos aplica los cambios y los registra en el historial
Passed Command-02: DeshacerUltimo() revierte el último comando ejecutado
Total tests: 14
     Passed: 14
```

### 4.3 Por patrón individual

| Patrón         | Comando                                          |
|----------------|--------------------------------------------------|
| Builder        | `dotnet test --filter "BuilderTests"`            |
| Factory Method | `dotnet test --filter "FactoryMethodTests"`      |
| Singleton      | `dotnet test --filter "SingletonTests"`          |
| Decorator      | `dotnet test --filter "DecoratorTests"`          |
| Strategy       | `dotnet test --filter "StrategyTests"`           |
| Facade         | `dotnet test --filter "FacadeTests"`             |
| Command        | `dotnet test --filter "CommandTests"`            |

Cada uno debe reportar `Passed: 2`.

## 5. Catálogo de los 14 casos de prueba

### 5.1 Builder (2)

| ID          | Qué verifica |
|-------------|--------------|
| Builder-01  | Que el builder fluido permita construir un `Estudiante` con todos los campos opcionales (telefono, dirección, lista de cursos, promedio). |
| Builder-02  | Que el `DirectorEstudiantes.CrearEstudianteNuevoIngreso(...)` aplique correctamente la receta predefinida (correo derivado, promedio 0, activo=true). |

### 5.2 Factory Method (2)

| ID                | Qué verifica |
|-------------------|--------------|
| FactoryMethod-01  | Que cada `Generador*` instancie su `IReporte` concreto correspondiente (`ReporteCalificaciones`, `ReporteAsistencia`, `ReporteFinanciero`). |
| FactoryMethod-02  | Que `Procesar()` integre el reporte creado por el factory method en la salida final, demostrando el contrato `Creator → Product`. |

### 5.3 Singleton (2)

| ID            | Qué verifica |
|---------------|--------------|
| Singleton-01  | Que dos llamadas a `ConfiguracionSistema.Instance` retornan la **misma** instancia (`Assert.Same`). |
| Singleton-02  | Que las modificaciones a través de una referencia se reflejan en cualquier otra, comprobando el estado compartido. |

### 5.4 Decorator (2)

| ID            | Qué verifica |
|---------------|--------------|
| Decorator-01  | Que `NotificacionBase` por sí sola produce solo el canal InApp. |
| Decorator-02  | Que apilar `EmailDecorator → SmsDecorator → WhatsAppDecorator` añade cada canal en orden, demostrando composición sin herencia. |

### 5.5 Strategy (2)

| ID           | Qué verifica |
|--------------|--------------|
| Strategy-01  | Que `PromedioPonderado` aplica los pesos exactos: `4·0.3 + 3·0.3 + 5·0.4 = 4.1`. |
| Strategy-02  | Que `CalculadoraNotaFinal.CambiarPolitica(...)` permite intercambiar la estrategia en tiempo de ejecución y obtener resultados distintos sobre las mismas notas. |

### 5.6 Facade (2)

| ID         | Qué verifica |
|------------|--------------|
| Facade-01  | Que una matrícula válida es orquestada correctamente por `MatriculaFacade.Matricular(...)` y produce registro + notificación. |
| Facade-02  | Que cuando no hay cupos disponibles, la facade aborta sin registrar y notifica el motivo, ocultando el control entre subsistemas al cliente. |

### 5.7 Command (2)

| ID          | Qué verifica |
|-------------|--------------|
| Command-01  | Que `GestorMatriculas.Ejecutar(...)` aplica los efectos en el `RegistroMatriculas` y los registra en el historial (`Stack<ICommand>`). |
| Command-02  | Que `DeshacerUltimo()` revierte el último comando ejecutado y reduce el historial, demostrando undo. |

## 6. Trazabilidad por commits

El historial de la rama `development` muestra el desarrollo incremental,
un commit por patrón:

```
git log --oneline --grep="gof"
```

Salida esperada (orden cronológico inverso):

```
e2d1703 feat(gof/command): add MatricularCommand + Gestor (undo) + 2 unit tests
139cd8a feat(gof/facade): add MatriculaFacade + 2 unit tests
df3012a feat(gof/strategy): add IPoliticaCalificacion strategies + 2 unit tests
0759913 feat(gof/decorator): add notification channel decorators + 2 unit tests
0b7efc8 feat(gof/singleton): add ConfiguracionSistema + 2 unit tests
5348afe feat(gof/factory-method): add GeneradorReporte hierarchy + 2 unit tests
bbe0d09 feat(gof/builder): add EstudianteBuilder + Director + 2 unit tests
e9dcf39 chore(gof): scaffold .NET 8 solution for Patrones GoF activity
6d35493 docs(gof): add conceptual document with the 23 GoF patterns
```

## 7. Resolución de problemas comunes

| Problema | Solución |
|----------|----------|
| `dotnet: command not found` (Git Bash recién instalado .NET) | Cerrar y abrir Git Bash, o ejecutar `export PATH="$PATH:/c/Program Files/dotnet"`. |
| `error NU1100: Unable to resolve …` | Falta la fuente nuget.org. Ejecutar `dotnet nuget add source https://api.nuget.org/v3/index.json --name nuget.org`. |
| Tildes mal mostradas en consola Windows | Ejecutar `chcp 65001` antes de `dotnet test`. |

## 8. Evidencia esperada (pantallazo)

El docente debe ver, como mínimo:

```
Passed!  - Failed: 0, Passed: 14, Skipped: 0, Total: 14
```

Para una entrega más completa, incluya además el **listado detallado de las 14
pruebas** generado por la sección 4.2.
