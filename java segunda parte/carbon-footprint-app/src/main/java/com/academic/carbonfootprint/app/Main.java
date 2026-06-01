package com.academic.carbonfootprint.app;

import com.academic.carbonfootprint.persistence.CsvCarbonFootprintRepository;
import com.academic.carbonfootprint.service.CarbonFootprintApplicationService;
import com.academic.carbonfootprint.ui.ConsoleMenu;

import java.nio.file.Path;

/**
 * Punto de entrada: configura ruta de persistencia y lanza el menú interactivo.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Path storage = Path.of("data", "carbon-footprints.csv");
        if (args != null && args.length >= 1 && !args[0].isBlank()) {
            storage = Path.of(args[0]);
        }

        var repository = new CsvCarbonFootprintRepository(storage);
        var service = new CarbonFootprintApplicationService(repository);
        var menu = new ConsoleMenu(service);
        menu.run();
    }
}
