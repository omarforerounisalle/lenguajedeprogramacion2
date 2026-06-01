package com.academic.carbonfootprint.persistence;

import com.academic.carbonfootprint.contract.CarbonFootprint;
import com.academic.carbonfootprint.model.Bicycle;
import com.academic.carbonfootprint.model.Building;
import com.academic.carbonfootprint.model.Car;
import com.academic.carbonfootprint.model.FuelType;

import java.nio.file.Path;
import java.util.Locale;

/**
 * Formato CSV (delimitador ';') con primera columna discriminadora (TYPE).
 * Se prioriza legibilidad y pruebas sobre rendimiento.
 */
public class CsvCarbonFootprintRepository extends AbstractTextCarbonFootprintRepository {

    private static final char SEP = ';';

    public CsvCarbonFootprintRepository(Path storagePath) {
        super(storagePath);
    }

    @Override
    protected String serializeRecord(CarbonFootprint item) {
        if (item instanceof Building b) {
            return String.join(String.valueOf(SEP),
                    b.getKindCode(),
                    escape(b.getDisplayName()),
                    Double.toString(b.getFloorAreaM2()),
                    Double.toString(b.getAnnualElectricityKwh()),
                    Double.toString(b.getAnnualNaturalGasM3()),
                    Integer.toString(b.getYearBuilt())
            );
        }
        if (item instanceof Car c) {
            return String.join(String.valueOf(SEP),
                    c.getKindCode(),
                    escape(c.getDisplayName()),
                    Double.toString(c.getAnnualKm()),
                    c.getFuelType().name(),
                    Double.toString(c.getLitersPer100Km()),
                    Integer.toString(c.getModelYear())
            );
        }
        if (item instanceof Bicycle bi) {
            return String.join(String.valueOf(SEP),
                    bi.getKindCode(),
                    escape(bi.getDisplayName()),
                    Double.toString(bi.getAnnualKm()),
                    Double.toString(bi.getBicycleWeightKg()),
                    Integer.toString(bi.getPurchaseYear())
            );
        }
        throw new IllegalArgumentException("Tipo no serializable: " + item.getClass().getName());
    }

    @Override
    protected CarbonFootprint deserializeRecord(String line) {
        String[] parts = line.split(String.valueOf(SEP), -1);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Formato incompleto.");
        }
        String kind = parts[0].trim().toUpperCase(Locale.ROOT);
        return switch (kind) {
            case "BUILDING" -> parseBuilding(parts);
            case "CAR" -> parseCar(parts);
            case "BICYCLE" -> parseBicycle(parts);
            default -> throw new IllegalArgumentException("Tipo desconocido: " + kind);
        };
    }

    private static Building parseBuilding(String[] parts) {
        if (parts.length != 6) {
            throw new IllegalArgumentException("BUILDING requiere 6 campos.");
        }
        String name = unescape(parts[1]);
        double area = Double.parseDouble(parts[2]);
        double kwh = Double.parseDouble(parts[3]);
        double gas = Double.parseDouble(parts[4]);
        int year = Integer.parseInt(parts[5]);
        return new Building(name, area, kwh, gas, year);
    }

    private static Car parseCar(String[] parts) {
        if (parts.length != 6) {
            throw new IllegalArgumentException("CAR requiere 6 campos.");
        }
        String name = unescape(parts[1]);
        double km = Double.parseDouble(parts[2]);
        FuelType fuel = FuelType.valueOf(parts[3].trim().toUpperCase(Locale.ROOT));
        double l100 = Double.parseDouble(parts[4]);
        int year = Integer.parseInt(parts[5]);
        return new Car(name, km, fuel, l100, year);
    }

    private static Bicycle parseBicycle(String[] parts) {
        if (parts.length != 5) {
            throw new IllegalArgumentException("BICYCLE requiere 5 campos.");
        }
        String name = unescape(parts[1]);
        double km = Double.parseDouble(parts[2]);
        double kg = Double.parseDouble(parts[3]);
        int year = Integer.parseInt(parts[4]);
        return new Bicycle(name, km, kg, year);
    }

    private static String escape(String raw) {
        return raw.replace("\\", "\\\\").replace(";", "\\;");
    }

    private static String unescape(String raw) {
        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        for (int i = 0; i < raw.length(); i++) {
            char ch = raw.charAt(i);
            if (escape) {
                sb.append(ch);
                escape = false;
            } else if (ch == '\\') {
                escape = true;
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
