package com.academic.carbonfootprint.validation;

import com.academic.carbonfootprint.model.Building;
import com.academic.carbonfootprint.model.Car;
import com.academic.carbonfootprint.model.FuelType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationTest {

    @Test
    void rejectsBlankBuildingName() {
        assertThrows(ValidationException.class, () -> new Building("  ", 100, 1, 1, 2000));
    }

    @Test
    void rejectsNegativeElectricity() {
        assertThrows(ValidationException.class, () -> new Building("OK", 100, -1, 0, 2000));
    }

    @Test
    void rejectsInvalidYear() {
        assertThrows(ValidationException.class, () -> new Building("OK", 100, 1, 0, 3000));
    }

    @Test
    void rejectsGasolineCarWithZeroConsumption() {
        assertThrows(ValidationException.class, () -> new Car("X", 1000, FuelType.GASOLINE, 0, 2018));
    }

    @Test
    void allowsElectricCarWithZeroLitersPer100Km() {
        new Car("EV", 5000, FuelType.ELECTRIC, 0, 2023);
    }
}
