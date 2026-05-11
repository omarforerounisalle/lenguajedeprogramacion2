package com.academic.carbonfootprint.model;

import com.academic.carbonfootprint.contract.CarbonFootprint;
import com.academic.carbonfootprint.validation.Validators;

/**
 * Automóvil con kilometraje anual y tipo de motorización. Comportamiento propio:
 * litros anuales estimados y revisión periódica por uso.
 */
public class Car implements CarbonFootprint {

    private final String displayName;
    private final double annualKm;
    private final FuelType fuelType;
    private final double litersPer100Km;
    private final int modelYear;

    public Car(String displayName, double annualKm, FuelType fuelType, double litersPer100Km, int modelYear) {
        this.displayName = Validators.requireNonBlank(displayName, "Nombre del vehículo");
        this.annualKm = Validators.requireNonNegative(annualKm, "Kilometraje anual");
        this.fuelType = Validators.requireNonNull(fuelType, "Tipo de combustible");
        this.litersPer100Km = Validators.requireNonNegative(litersPer100Km, "Litros cada 100 km");
        if (fuelType != FuelType.ELECTRIC && litersPer100Km <= 0) {
            throw new com.academic.carbonfootprint.validation.ValidationException(
                    "Litros/100 km debe ser > 0 para combustión interna.");
        }
        this.modelYear = Validators.requireYearInRange(modelYear, "Año del modelo");
    }

    /**
     * Huella por uso (tailpipe/upstream simplificado): km × intensidad del tipo de motorización.
     */
    @Override
    public double getCarbonFootprint() {
        return annualKm * (fuelType.getGramsCo2ePerKm() / 1000.0);
    }

    @Override
    public String getKindCode() {
        return "CAR";
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    /** Combustible líquido estimado anual (sin efecto para eléctrico en este modelo simplificado). */
    public double estimatedAnnualFuelLiters() {
        if (fuelType == FuelType.ELECTRIC) {
            return 0.0;
        }
        return (annualKm / 100.0) * litersPer100Km;
    }

    /** Heurística docente: si supera umbral de km, conviene agenda de mantenimiento. */
    public boolean needsMaintenanceAttention(double annualKmThreshold) {
        Validators.requireNonNegative(annualKmThreshold, "Umbral de km");
        return annualKm >= annualKmThreshold;
    }

    public double getAnnualKm() {
        return annualKm;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public double getLitersPer100Km() {
        return litersPer100Km;
    }

    public int getModelYear() {
        return modelYear;
    }

    @Override
    public String toString() {
        return String.format(
                "Car[%s | %.0f km/a | %s | %.1f L/100km | año %d]",
                displayName, annualKm, fuelType.name(), litersPer100Km, modelYear
        );
    }
}
