package com.academic.carbonfootprint.contract;

/**
 * Contrato funcional del caso de estudio: cada entidad calcula su huella anual de GEI.
 * <p>
 * Las implementaciones concretas ({@code Building}, {@code Car}, {@code Bicycle}) no comparten
 * una jerarquía de clases entre sí (no hay {@code extends} entre ellas), lo cual respeta el enunciado.
 * El polimorfismo se materializa al manipular referencias de tipo {@code CarbonFootprint}.
 */
public interface CarbonFootprint {

    /**
     * @return huella de carbono equivalente anual en kg CO<sub>2</sub>e (aproximación docente).
     */
    double getCarbonFootprint();

    /** Etiqueta corta para reportes y CSV (p.ej. BUILDING). */
    String getKindCode();

    /** Nombre legible para consola y persistencia. */
    String getDisplayName();
}
