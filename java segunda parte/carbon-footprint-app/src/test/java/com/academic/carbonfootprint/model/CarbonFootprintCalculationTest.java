package com.academic.carbonfootprint.model;

import com.academic.carbonfootprint.calculation.EmissionFactors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarbonFootprintCalculationTest {

    @Test
    void buildingFootprint_matchesElectricityAndGasFormula() {
        Building b = new Building("Lab", 500.0, 10_000.0, 5_000.0, 2005);
        double expected = 10_000.0 * EmissionFactors.KG_CO2E_PER_KWH_ELECTRICITY
                + 5_000.0 * EmissionFactors.KG_CO2E_PER_M3_NATURAL_GAS;
        assertEquals(expected, b.getCarbonFootprint(), 1e-9);
    }

    @Test
    void carFootprint_usesGramsPerKmFromFuelType() {
        Car car = new Car("UnitCar", 10_000.0, FuelType.GASOLINE, 7.0, 2015);
        double expected = 10_000.0 * (FuelType.GASOLINE.getGramsCo2ePerKm() / 1000.0);
        assertEquals(expected, car.getCarbonFootprint(), 1e-9);
    }

    @Test
    void bicycleFootprint_isSmallComparedToCar() {
        Bicycle bike = new Bicycle("UnitBike", 3_000.0, 14.0, 2022);
        Car car = new Car("UnitCarSmall", 3_000.0, FuelType.GASOLINE, 6.0, 2022);
        assertTrue(bike.getCarbonFootprint() < car.getCarbonFootprint());
    }
}
