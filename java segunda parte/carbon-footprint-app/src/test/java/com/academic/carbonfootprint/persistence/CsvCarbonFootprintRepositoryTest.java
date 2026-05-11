package com.academic.carbonfootprint.persistence;

import com.academic.carbonfootprint.contract.CarbonFootprint;
import com.academic.carbonfootprint.model.Bicycle;
import com.academic.carbonfootprint.model.Building;
import com.academic.carbonfootprint.model.Car;
import com.academic.carbonfootprint.model.FuelType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class CsvCarbonFootprintRepositoryTest {

    @Test
    void roundTrip_preservesTypesAndKeyFields(@TempDir Path temp) throws IOException {
        Path file = temp.resolve("store.csv");
        CsvCarbonFootprintRepository repo = new CsvCarbonFootprintRepository(file);

        List<CarbonFootprint> original = new ArrayList<>();
        original.add(new Building("HQ", 300, 20_000, 8_000, 2010));
        original.add(new Car("Fleet", 12_000, FuelType.ELECTRIC, 0, 2024));
        original.add(new Bicycle("City", 1500, 13.0, 2019));

        repo.saveAll(original);
        List<CarbonFootprint> loaded = repo.loadAll();

        assertEquals(3, loaded.size());
        assertInstanceOf(Building.class, loaded.get(0));
        assertInstanceOf(Car.class, loaded.get(1));
        assertInstanceOf(Bicycle.class, loaded.get(2));

        Building b = (Building) loaded.get(0);
        assertEquals("HQ", b.getDisplayName());
        assertEquals(20_000.0, b.getAnnualElectricityKwh(), 1e-9);

        Car c = (Car) loaded.get(1);
        assertEquals(FuelType.ELECTRIC, c.getFuelType());

        Bicycle bi = (Bicycle) loaded.get(2);
        assertEquals(1500.0, bi.getAnnualKm(), 1e-9);
    }

    @Test
    void loadMissingFile_returnsEmptyList(@TempDir Path temp) throws IOException {
        Path file = temp.resolve("missing.csv");
        CsvCarbonFootprintRepository repo = new CsvCarbonFootprintRepository(file);
        assertEquals(0, repo.loadAll().size());
    }
}
