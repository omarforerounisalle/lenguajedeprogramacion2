package com.academic.carbonfootprint.persistence;

import com.academic.carbonfootprint.contract.CarbonFootprint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Herencia académica (Template Method): el flujo de lectura/escritura es común; el formato lo define la subclase.
 * Esto respeta el enunciado: {@code Building}/{@code Car}/{@code Bicycle} siguen sin heredar entre sí.
 */
public abstract class AbstractTextCarbonFootprintRepository implements CarbonFootprintRepository {

    private final Path storagePath;

    protected AbstractTextCarbonFootprintRepository(Path storagePath) {
        this.storagePath = storagePath;
    }

    public Path getStoragePath() {
        return storagePath;
    }

    @Override
    public final void saveAll(List<CarbonFootprint> items) throws IOException {
        Path parent = storagePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
                storagePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        )) {
            for (CarbonFootprint item : items) {
                writer.write(serializeRecord(item));
                writer.newLine();
            }
        }
    }

    @Override
    public final List<CarbonFootprint> loadAll() throws IOException {
        if (!Files.exists(storagePath)) {
            return new ArrayList<>();
        }
        List<CarbonFootprint> result = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(storagePath, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                try {
                    result.add(deserializeRecord(trimmed));
                } catch (RuntimeException ex) {
                    throw new IOException("Error parseando línea " + lineNumber + ": " + ex.getMessage(), ex);
                }
            }
        }
        return result;
    }

    /** Serializa un objeto de dominio a una línea de texto. */
    protected abstract String serializeRecord(CarbonFootprint item);

    /** Deserializa una línea previamente producida por {@link #serializeRecord(CarbonFootprint)}. */
    protected abstract CarbonFootprint deserializeRecord(String line);
}
