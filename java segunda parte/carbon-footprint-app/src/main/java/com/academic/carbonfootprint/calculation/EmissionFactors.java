package com.academic.carbonfootprint.calculation;

/**
 * Factores de emisión simplificados para fines académicos (órdenes de magnitud realistas).
 * <p>
 * <strong>Aviso:</strong> no sustituyen inventarios nacionales ni bases de datos sectoriales;
 * sirven para demostrar modularidad y documentación de supuestos.
 */
public final class EmissionFactors {

    private EmissionFactors() {
    }

    /** kg CO2e por kWh eléctrico (mezcla grid típica; ejercicio docente). */
    public static final double KG_CO2E_PER_KWH_ELECTRICITY = 0.42;

    /** kg CO2e por m3 de gas natural (combustión residencial simplificada). */
    public static final double KG_CO2E_PER_M3_NATURAL_GAS = 2.02;

    /**
     * Gramos CO2e por km por tipo de motorización (valores orientativos para comparar escenarios).
     */
    public static final double G_CO2E_PER_KM_GASOLINE_CAR = 130.0;
    public static final double G_CO2E_PER_KM_DIESEL_CAR = 125.0;
    /** Incluye upstream energético de forma muy simplificada (mix eléctrico). */
    public static final double G_CO2E_PER_KM_ELECTRIC_CAR = 35.0;

    /** Amortización anualizada de fabricación de bicicleta (kg CO2e/año), orden reducido. */
    public static final double BICYCLE_MANUFACTURING_ANNUAL_KG = 18.0;

    /** Mantenimiento/cadena/neumáticos por km (g CO2e/km), muy pequeño frente a automóvil. */
    public static final double G_CO2E_PER_KM_BICYCLE_UPKEEP = 6.0;
}
