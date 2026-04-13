# Museo (Java)

Port del ejercicio **Museo** alineado con el UML y con la logica de `python/museo.py`.

## Requisitos

- JDK 8 o superior (`javac` y `java` en el PATH).

## Compilar y ejecutar la demostracion

Desde la carpeta `java` del repositorio:

**Windows (PowerShell o CMD)**

```text
mkdir out 2>nul
javac -encoding UTF-8 -d out src/museo/*.java
java -cp out museo.MuseoDemo
```

**Linux / macOS**

```bash
mkdir -p out
javac -encoding UTF-8 -d out src/museo/*.java
java -cp out museo.MuseoDemo
```

## Paquete

- Fuentes: `src/museo/`
- Clase ejecutable de ejemplo: `museo.MuseoDemo`

## Estructura de clases (resumen)

- Abstractas: `Usuario`, `ObraArte`
- Obras: `Cuadro`, `Escultura`, `OtroObjeto`
- Dominio: `CatalogoObras`, `Sala`, `Restauracion`, `Cesion`, `MuseoColaborador`, `MonitorVestibulo`, `Visitante`, `ProcesoRestauracionAutomatica`
- Roles: `EncargadoCatalogo`, `Director`, `RestauradorJefe`
- Util: `EstadosMuseo`, `FechasUtil`
