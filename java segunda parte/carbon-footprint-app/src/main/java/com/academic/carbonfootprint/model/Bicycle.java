package com.academic.carbonfootprint.model;

import com.academic.carbonfootprint.calculation.EmissionFactors;
import com.academic.carbonfootprint.contract.CarbonFootprint;
import com.academic.carbonfootprint.validation.Validators;

/**
 * Bicicleta con uso anual y masa aproximada (impacto producción amortizado).
 * Comportamiento propio: clasificación de bajo impacto relativo frente a umbral.
 */
public class Bicycle implements CarbonFootprint {

    private final String displayName;
    private final double annualKm;
    private final double bicycleWeightKg;
    private final int purchaseYear;

    public Bicycle(String displayName, double annualKm, double bicycleWeightKg, int purchaseYear) {
        this.displayName = Validators.requireNonBlank(displayName, "Nombre de la bicicleta");
        this.annualKm = Validators.requireNonNegative(annualKm, "Kilometraje anual");
        this.bicycleWeightKg = Validators.requireNonNegative(bicycleWeightKg, "Peso de la bicicleta");
        if (this.bicycleWeightKg > 0 && this.bicycleWeightKg < 4) {
            throw new com.academic.carbonfootprint.validation.ValidationException(
                    "Peso de bicicleta irrealmente bajo (< 4 kg).");
        }
        this.purchaseYear = Validators.requireYearInRange(purchaseYear, "Año de compra");
    }

    /**
     * Huella muy inferior al automóvil: amortización fabricación + mantenimiento por km.
     */
    @Override
    public double getCarbonFootprint() {
        double effectiveKg = bicycleWeightKg <= 0 ? 12.0 : bicycleWeightKg;
        double manufacturingShare = EmissionFactors.BICYCLE_MANUFACTURING_ANNUAL_KG + effectiveKg * 0.35;
        double upkeepKg = annualKm * (EmissionFactors.G_CO2E_PER_KM_BICYCLE_UPKEEP / 1000.0);
        return manufacturingShare + upkeepKg;
    }

    @Override
    public String getKindCode() {
        return "BICYCLE";
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    /** Si la huella total es menor que el umbral (kg CO2e/año), se etiqueta como muy baja. */
    public boolean isUltraLowImpact(double annualThresholdKg) {
        Validators.requireNonNegative(annualThresholdKg, "Umbral anual");
        return getCarbonFootprint() < annualThresholdKg;
    }

    /** Estimación orientativa de energía humana (kcal/año) solo para análisis cualitativo. */
    public double roughHumanEnergyKcalPerYear() {
        return annualKm * 30.0;
    }

    public double getAnnualKm() {
        return annualKm;
    }

    public double getBicycleWeightKg() {
        return bicycleWeightKg;
    }

    public int getPurchaseYear() {
        return purchaseYear;
    }

    @Override
    public String toString() {
        return String.format(
                "Bicycle[%s | %.0f km/a | %.1f kg | compra %d]",
                displayName, annualKm, bicycleWeightKg, purchaseYear
        );
    }
}
