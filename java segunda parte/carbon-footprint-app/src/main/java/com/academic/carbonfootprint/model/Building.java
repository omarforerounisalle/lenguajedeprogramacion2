package com.academic.carbonfootprint.model;

import com.academic.carbonfootprint.calculation.EmissionFactors;
import com.academic.carbonfootprint.contract.CarbonFootprint;
import com.academic.carbonfootprint.validation.Validators;

/**
 * Edificio con consumos energéticos anuales (eléctrico + gas). Comportamiento propio:
 * intensidad energética por área y estimación orientativa de coste anual.
 */
public class Building implements CarbonFootprint {

    private final String displayName;
    private final double floorAreaM2;
    private final double annualElectricityKwh;
    private final double annualNaturalGasM3;
    private final int yearBuilt;

    public Building(String displayName, double floorAreaM2, double annualElectricityKwh,
                    double annualNaturalGasM3, int yearBuilt) {
        this.displayName = Validators.requireNonBlank(displayName, "Nombre del edificio");
        this.floorAreaM2 = Validators.requireNonNegative(floorAreaM2, "Superficie (m²)");
        this.annualElectricityKwh = Validators.requireNonNegative(annualElectricityKwh, "Electricidad anual (kWh)");
        this.annualNaturalGasM3 = Validators.requireNonNegative(annualNaturalGasM3, "Gas natural anual (m³)");
        this.yearBuilt = Validators.requireYearInRange(yearBuilt, "Año de construcción");
    }

    @Override
    public double getCarbonFootprint() {
        double electricityKg = annualElectricityKwh * EmissionFactors.KG_CO2E_PER_KWH_ELECTRICITY;
        double gasKg = annualNaturalGasM3 * EmissionFactors.KG_CO2E_PER_M3_NATURAL_GAS;
        return electricityKg + gasKg;
    }

    @Override
    public String getKindCode() {
        return "BUILDING";
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    /** kWh/m²·año: indicador simple de intensidad (no sustituye auditoría energética). */
    public double annualElectricityIntensityKwhPerM2() {
        if (floorAreaM2 <= 0) {
            return 0;
        }
        return annualElectricityKwh / floorAreaM2;
    }

    /** Coste anual aproximado de electricidad (moneda local implícita). */
    public double estimatedAnnualElectricityCost(double pricePerKwh) {
        Validators.requireNonNegative(pricePerKwh, "Precio por kWh");
        return annualElectricityKwh * pricePerKwh;
    }

    public double getFloorAreaM2() {
        return floorAreaM2;
    }

    public double getAnnualElectricityKwh() {
        return annualElectricityKwh;
    }

    public double getAnnualNaturalGasM3() {
        return annualNaturalGasM3;
    }

    public int getYearBuilt() {
        return yearBuilt;
    }

    @Override
    public String toString() {
        return String.format(
                "Building[%s | %.1f m² | %.0f kWh/a | %.1f m³ gas/a | año %d]",
                displayName, floorAreaM2, annualElectricityKwh, annualNaturalGasM3, yearBuilt
        );
    }
}
