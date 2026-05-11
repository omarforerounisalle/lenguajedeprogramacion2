package com.academic.carbonfootprint.polymorphism;

import com.academic.carbonfootprint.contract.CarbonFootprint;
import com.academic.carbonfootprint.model.Bicycle;
import com.academic.carbonfootprint.model.Building;
import com.academic.carbonfootprint.model.Car;
import com.academic.carbonfootprint.model.FuelType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica el uso académico de {@link ArrayList}{@code <}{@link CarbonFootprint}{@code >} y despacho dinámico.
 */
class PolymorphismTest {

    @Test
    void arrayListOfInterfaceInvokesConcreteImplementations() {
        ArrayList<CarbonFootprint> list = new ArrayList<>();
        list.add(new Building("A", 200, 1000, 500, 1999));
        list.add(new Car("B", 5000, FuelType.DIESEL, 5.5, 2017));
        list.add(new Bicycle("C", 800, 11.0, 2020));

        double sum = 0;
        for (CarbonFootprint cf : list) {
            sum += cf.getCarbonFootprint();
        }

        assertEquals(3, list.size());
        assertTrue(sum > 0);
        assertEquals("BUILDING", list.get(0).getKindCode());
        assertEquals("CAR", list.get(1).getKindCode());
        assertEquals("BICYCLE", list.get(2).getKindCode());
    }
}
