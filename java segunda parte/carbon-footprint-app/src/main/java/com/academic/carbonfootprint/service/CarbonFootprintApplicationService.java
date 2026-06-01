package com.academic.carbonfootprint.service;

import com.academic.carbonfootprint.contract.CarbonFootprint;
import com.academic.carbonfootprint.model.Bicycle;
import com.academic.carbonfootprint.model.Building;
import com.academic.carbonfootprint.model.Car;
import com.academic.carbonfootprint.model.FuelType;
import com.academic.carbonfootprint.persistence.CarbonFootprintRepository;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Caso de uso principal: mantiene {@link ArrayList} polimórfica y coordina persistencia.
 */
public class CarbonFootprintApplicationService {

    private final ArrayList<CarbonFootprint> footprints = new ArrayList<>();
    private final CarbonFootprintRepository repository;

    public CarbonFootprintApplicationService(CarbonFootprintRepository repository) {
        this.repository = repository;
    }

    public void addBuilding(Building building) {
        footprints.add(building);
    }

    public void addCar(Car car) {
        footprints.add(car);
    }

    public void addBicycle(Bicycle bicycle) {
        footprints.add(bicycle);
    }

    /** Vista inmutable para otros componentes. */
    public List<CarbonFootprint> getFootprints() {
        return Collections.unmodifiableList(footprints);
    }

    /** Lista explícita solicitada por el enunciado académico (tipo parametrizado polimórfico). */
    public ArrayList<CarbonFootprint> getFootprintsAsArrayList() {
        return new ArrayList<>(footprints);
    }

    public void clearAll() {
        footprints.clear();
    }

    public void saveToDisk() throws IOException {
        repository.saveAll(footprints);
    }

    public void loadFromDisk() throws IOException {
        List<CarbonFootprint> loaded = repository.loadAll();
        footprints.clear();
        footprints.addAll(loaded);
    }

    /**
     * Demuestra polimorfismo: referencias {@link CarbonFootprint} invocan implementaciones concretas en tiempo de ejecución.
     */
    public void printFootprintReport(PrintStream out) {
        out.println();
        out.println("=== REPORTE DE HUELLAS (polimorfismo sobre CarbonFootprint) ===");
        if (footprints.isEmpty()) {
            out.println("(sin objetos registrados)");
            return;
        }
        int index = 1;
        for (CarbonFootprint footprint : footprints) {
            double kg = footprint.getCarbonFootprint();
            out.printf(Locale.US,
                    "%d) [%s] %s -> %.3f kg CO2e / año%n",
                    index++,
                    footprint.getKindCode(),
                    footprint.getDisplayName(),
                    kg
            );
            out.printf(Locale.US, "    detalle: %s%n", footprint.toString());
        }
        out.println("============================================================");
        out.println();
    }

    public void seedMinimumExerciseSample() {
        footprints.clear();
        footprints.add(new Building("Demo Building", 420.0, 55_000.0, 12_000.0, 1998));
        footprints.add(new Car("Demo Car", 14_000.0, FuelType.GASOLINE, 7.2, 2018));
        footprints.add(new Bicycle("Demo Bicycle", 2_400.0, 13.5, 2021));
    }
}
