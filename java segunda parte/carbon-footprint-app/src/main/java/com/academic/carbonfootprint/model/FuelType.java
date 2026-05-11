package com.academic.carbonfootprint.model;

import com.academic.carbonfootprint.calculation.EmissionFactors;

/**
 * Clasificación simple del automóvil para seleccionar intensidad de emisión por km.
 */
public enum FuelType {
    GASOLINE(EmissionFactors.G_CO2E_PER_KM_GASOLINE_CAR),
    DIESEL(EmissionFactors.G_CO2E_PER_KM_DIESEL_CAR),
    ELECTRIC(EmissionFactors.G_CO2E_PER_KM_ELECTRIC_CAR);

    private final double gramsCo2ePerKm;

    FuelType(double gramsCo2ePerKm) {
        this.gramsCo2ePerKm = gramsCo2ePerKm;
    }

    public double getGramsCo2ePerKm() {
        return gramsCo2ePerKm;
    }
}
