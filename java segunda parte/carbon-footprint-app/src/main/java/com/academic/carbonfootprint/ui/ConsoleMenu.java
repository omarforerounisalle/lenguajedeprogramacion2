package com.academic.carbonfootprint.ui;

import com.academic.carbonfootprint.model.Bicycle;
import com.academic.carbonfootprint.model.Building;
import com.academic.carbonfootprint.model.Car;
import com.academic.carbonfootprint.model.FuelType;
import com.academic.carbonfootprint.service.CarbonFootprintApplicationService;
import com.academic.carbonfootprint.validation.ValidationException;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Menú consola con validación de opciones y mensajes claros (experiencia “informe”).
 */
public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final CarbonFootprintApplicationService service;

    public ConsoleMenu(CarbonFootprintApplicationService service) {
        this.service = service;
    }

    public void run() {
        printHeader();
        boolean running = true;
        while (running) {
            printOptions();
            System.out.print("Seleccione una opción: ");
            String raw = scanner.nextLine();
            try {
                int option = Integer.parseInt(raw.trim());
                running = handleOption(option);
            } catch (NumberFormatException ex) {
                System.out.println("Opción no numérica. Intente de nuevo.");
            } catch (ValidationException ex) {
                System.out.println("Validación: " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("Error de archivo: " + ex.getMessage());
            }
        }
    }

    private boolean handleOption(int option) throws IOException {
        switch (option) {
            case 1 -> promptBuilding();
            case 2 -> promptCar();
            case 3 -> promptBicycle();
            case 4 -> showAll();
            case 5 -> service.printFootprintReport(System.out);
            case 6 -> {
                service.saveToDisk();
                System.out.println("Datos guardados correctamente.");
            }
            case 7 -> {
                service.loadFromDisk();
                System.out.println("Datos cargados desde disco.");
            }
            case 8 -> {
                service.seedMinimumExerciseSample();
                System.out.println("Se cargó el ejemplo mínimo del enunciado (3 objetos).");
            }
            case 0 -> {
                System.out.println("Saliendo. ¡Hasta pronto!");
                return false;
            }
            default -> System.out.println("Opción inválida.");
        }
        return true;
    }

    private void promptBuilding() {
        System.out.print("Nombre del edificio: ");
        String name = scanner.nextLine();
        double area = readDouble("Superficie útil (m²): ");
        double kwh = readDouble("Consumo eléctrico anual (kWh): ");
        double gas = readDouble("Consumo gas natural anual (m³): ");
        int year = readInt("Año de construcción: ");
        service.addBuilding(new Building(name, area, kwh, gas, year));
        System.out.println("Edificio agregado.");
    }

    private void promptCar() {
        System.out.print("Nombre del vehículo: ");
        String name = scanner.nextLine();
        double km = readDouble("Kilometraje anual (km): ");
        FuelType fuel = readFuelType();
        double l100 = readDouble("Litros cada 100 km (use 0 si es ELÉCTRICO): ");
        int modelYear = readInt("Año del modelo: ");
        service.addCar(new Car(name, km, fuel, l100, modelYear));
        System.out.println("Automóvil agregado.");
    }

    private void promptBicycle() {
        System.out.print("Nombre / modelo bicicleta: ");
        String name = scanner.nextLine();
        double km = readDouble("Kilometraje anual (km): ");
        double kg = readDouble("Peso aproximado (kg, 0 = usar valor típico 12 kg): ");
        int year = readInt("Año de compra: ");
        service.addBicycle(new Bicycle(name, km, kg, year));
        System.out.println("Bicicleta agregada.");
    }

    private void showAll() {
        System.out.println();
        System.out.println("=== OBJETOS REGISTRADOS ===");
        if (service.getFootprints().isEmpty()) {
            System.out.println("(vacío)");
            return;
        }
        int i = 1;
        for (Object o : service.getFootprints()) {
            System.out.printf(Locale.US, "%d) %s%n", i++, o.toString());
        }
        System.out.println("===========================");
        System.out.println();
    }

    private FuelType readFuelType() {
        System.out.println("Tipo de motorización:");
        System.out.println(" 1) GASOLINE");
        System.out.println(" 2) DIESEL");
        System.out.println(" 3) ELECTRIC");
        int opt = readInt("Seleccione (1-3): ");
        return switch (opt) {
            case 1 -> FuelType.GASOLINE;
            case 2 -> FuelType.DIESEL;
            case 3 -> FuelType.ELECTRIC;
            default -> throw new ValidationException("Selección de combustible inválida.");
        };
    }

    private double readDouble(String label) {
        System.out.print(label);
        String line = scanner.nextLine();
        try {
            return Double.parseDouble(line.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            throw new ValidationException("Número decimal inválido para: " + label.trim());
        }
    }

    private int readInt(String label) {
        System.out.print(label);
        String line = scanner.nextLine();
        try {
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException ex) {
            throw new ValidationException("Entero inválido para: " + label.trim());
        }
    }

    private void printHeader() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  Carbon Footprint Lab — Estudio de caso (POO + ficheros)   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    private void printOptions() {
        System.out.println();
        System.out.println("1) Crear edificio (Building)");
        System.out.println("2) Crear automóvil (Car)");
        System.out.println("3) Crear bicicleta (Bicycle)");
        System.out.println("4) Mostrar todos los objetos");
        System.out.println("5) Calcular huellas (polimorfismo)");
        System.out.println("6) Guardar datos (CSV)");
        System.out.println("7) Cargar datos (CSV)");
        System.out.println("8) Cargar ejemplo mínimo del enunciado (3 objetos)");
        System.out.println("0) Salir");
        System.out.println();
    }
}
