# Carbon Footprint Lab — Estudio de caso (Java 17 + Maven)

Proyecto académico que cumple y amplía el enunciado de huella de carbono anual (kg CO₂e/año aproximados), integrando **polimorfismo**, **persistencia en texto**, **herencia en infraestructura**, **JUnit 5** y **menú consola**.

---

## 1. Análisis de requisitos

### Funcionales

- Modelar tres entidades **sin herencia entre sí**: `Building`, `Car`, `Bicycle`, cada una con atributos y comportamientos propios.
- Interfaz `CarbonFootprint` con `getCarbonFootprint()`.
- Colección **`ArrayList<CarbonFootprint>`** y recorrido polimórfico imprimiendo identificación + huella.
- Persistencia **CSV** (texto) con carga/guardado completo de objetos creados.
- Menú consola: alta por tipo, listado, cálculo masivo, guardar/cargar, salida.

### No funcionales

- Código modular por paquetes; encapsulación; manejo explícito de excepciones de validación y E/S.
- Factores de emisión documentados como **aproximación docente** (no inventario oficial).

### Dónde aparece cada concepto OOP

| Concepto | Implementación |
|----------|----------------|
| **Polimorfismo** | Referencias `CarbonFootprint` apuntan a `Building`/`Car`/`Bicycle`; en tiempo de ejecución se ejecuta el `getCarbonFootprint()` correspondiente (dynamic dispatch). Ver `CarbonFootprintApplicationService#printFootprintReport`. |
| **Modularidad / reutilización** | Paquetes `model`, `calculation`, `validation`, `persistence`, `service`, `ui`; factores centralizados en `EmissionFactors`; validaciones en `Validators`. |
| **Herencia (sin violar el enunciado)** | `Building`/`Car`/`Bicycle` **no** extienden entre sí. La herencia está en **persistencia**: `AbstractTextCarbonFootprintRepository` define plantilla de lectura/escritura; `CsvCarbonFootprintRepository` serializa filas (Template Method). |
| **Manejo de archivos** | `BufferedReader`/`BufferedWriter` mediante `Files.newBufferedReader` / `newBufferedWriter` (NIO.2), UTF-8. |

### Casos borde contemplados

- Nombres vacíos; números negativos; años fuera de rango; combustible gasolina/diesel con `litersPer100Km` ilegal; opciones de menú no numéricas o fuera de rango; CSV ausente (carga → lista vacía); líneas CSV corruptas (error con número de línea).

---

## 2. Arquitectura y diseño

### Paquetes (`com.academic.carbonfootprint.*`)

| Paquete | Responsabilidad |
|---------|-----------------|
| `contract` | Contrato `CarbonFootprint`. |
| `model` | Entidades de dominio: `Building`, `Car`, `Bicycle`, `FuelType`. |
| `calculation` | Constantes / factores documentados (`EmissionFactors`). |
| `validation` | Reglas y `ValidationException`. |
| `persistence` | Puerto `CarbonFootprintRepository`; plantilla abstracta + CSV. |
| `service` | Orquestación y reporte polimórfico. |
| `ui` | Menú consola. |
| `app` | `Main`. |

### Patrones

- **Template Method**: `AbstractTextCarbonFootprintRepository` fija el algoritmo guardar/cargar; la subclase define serialización por fila.
- **Dependency Inversion**: la aplicación depende de `CarbonFootprintRepository`, no del CSV concreto.

### Flujo de datos

```
Usuario → ConsoleMenu → CarbonFootprintApplicationService → model + repository (CSV en disco)
                      → impresión polimórfica sobre ArrayList<CarbonFootprint>
```

---

## 3. UML (explicación textual)

- **Realización**: `Building`, `Car`, `Bicycle` → interfaz `CarbonFootprint`.
- **Dependencia**: `CarbonFootprintApplicationService` → `CarbonFootprintRepository`.
- **Herencia**: `CsvCarbonFootprintRepository` → `AbstractTextCarbonFootprintRepository` → implementa `CarbonFootprintRepository`.

---

## 4. Fórmulas simplificadas (supuestos)

- **Building:** \( \text{kWh} \times EF_{\text{grid}} + \text{m³ gas} \times EF_{\text{gas}} \). Factores en `EmissionFactors`.
- **Car:** \( \text{km/año} \times (\text{g CO₂e/km}) / 1000 \) según `FuelType` (valores típicos a orden de magnitud).
- **Bicycle:** término base de fabricación/amortización + peso efectivo + mantenimiento por km (muy inferior al automóvil).

---

## 5. Formato de archivo (CSV)

- Delimitador **`;`**, primera columna discriminadora `BUILDING|CAR|BICYCLE`.
- **Por qué CSV:** legible, versionable, fácil de inspeccionar y probar (round-trip en tests).
- **Errores:** validación en dominio al construir objetos; en carga, líneas inválidas lanzan `IOException` con mensaje que incluye número de línea.

---

## 6. Cómo compilar y ejecutar

Requisitos: **JDK 17+** y **Maven 3.9+** en el `PATH`.

**Maven en esta máquina:** Apache Maven **3.9.6** está instalado en `C:\Users\omhef\tools\apache-maven-3.9.6` y su carpeta `bin` se añadió al **PATH del usuario**. Tras abrir una terminal nueva, `mvn -version` debe funcionar. El workspace de Cursor también antepone ese `bin` en `.vscode/settings.json`.

```bash
cd "java segunda parte/carbon-footprint-app"
mvn -q clean package
java -jar target/carbon-footprint-app-1.0.0-SNAPSHOT.jar
```

Archivo de datos por defecto: `data/carbon-footprints.csv` (relativo al directorio de trabajo).  
Ruta personalizada:

```bash
java -jar target/carbon-footprint-app-1.0.0-SNAPSHOT.jar "C:/temp/mis-datos.csv"
```

### Pruebas unitarias

```bash
mvn test
```

### Estrategia de testing

- Cálculos con valores esperados cerrados (assert con delta).
- CSV round-trip en directorio temporal (`@TempDir`).
- Validaciones que deben lanzar `ValidationException`.
- Lista polimórfica verificando tipos y suma positiva.

---

## 7. Ejemplo de interacción (extracto)

```
1) Crear edificio (Building)
...
5) Calcular huellas (polimorfismo)

=== REPORTE DE HUELLAS (polimorfismo sobre CarbonFootprint) ===
1) [BUILDING] Oficina Central -> 12345.678 kg CO2e / año
    detalle: Building[Oficina Central | ...
```

*(Los valores numéricos dependen de los datos ingresados.)*

---

## 8. Justificación académica final

- Se respeta el enunciado: **tres clases no relacionadas por herencia** entre sí, compartiendo solo la interfaz.
- Se satisface el requisito explícito de **herencia** ubicándola en capa de infraestructura (persistencia), patrón habitual en arquitecturas limpias (SRP + extensión sin contaminar el dominio).
- **Polimorfismo real** sobre `CarbonFootprint` con `ArrayList` parametrizada.
- **Persistencia textual** y **JUnit 5** cubren los entregables formales.

---

## 9. Mejoras futuras

- Internacionalización (i18n) de mensajes.
- Factor eléctrico dependiente de país/año (CSV de configuración).
- CLI con Picocli / GUI JavaFX opcional.
- Persistencia JSON con Jackson manteniendo el mismo puerto `CarbonFootprintRepository`.
