package co.edu.unisalle.crud.dao;

import co.edu.unisalle.crud.model.Estudiante;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del DAO que persiste estudiantes en un archivo JSON.
 *
 * SRP: única responsabilidad — leer y escribir estudiantes en JSON.
 * LSP: respeta el contrato declarado por {@link IEstudianteDAO}; cualquier
 * implementación puede sustituirla sin romper a los clientes.
 */
public class EstudianteDAOJson implements IEstudianteDAO {

    private final File archivo;
    private final ObjectMapper mapper;

    public EstudianteDAOJson(File archivo) {
        this.archivo = archivo;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        inicializarArchivoSiNoExiste();
    }

    private void inicializarArchivoSiNoExiste() {
        if (!archivo.exists()) {
            File padre = archivo.getParentFile();
            if (padre != null && !padre.exists()) {
                padre.mkdirs();
            }
            escribirTodos(new ArrayList<>());
        }
    }

    @Override
    public Estudiante crear(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }
        List<Estudiante> estudiantes = listar();
        boolean existe = estudiantes.stream().anyMatch(e -> e.getId() == estudiante.getId());
        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe un estudiante con id " + estudiante.getId());
        }
        estudiantes.add(estudiante);
        escribirTodos(estudiantes);
        return estudiante;
    }

    @Override
    public List<Estudiante> listar() {
        try {
            if (archivo.length() == 0) {
                return new ArrayList<>();
            }
            return mapper.readValue(archivo, new TypeReference<List<Estudiante>>() {});
        } catch (IOException e) {
            throw new UncheckedIOException("No fue posible leer el archivo JSON.", e);
        }
    }

    @Override
    public Optional<Estudiante> buscarPorId(int id) {
        return listar().stream()
                .filter(e -> e.getId() == id)
                .findFirst();
    }

    @Override
    public Estudiante actualizar(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }
        List<Estudiante> estudiantes = listar();
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getId() == estudiante.getId()) {
                estudiantes.set(i, estudiante);
                escribirTodos(estudiantes);
                return estudiante;
            }
        }
        throw new IllegalArgumentException(
                "No existe un estudiante con id " + estudiante.getId());
    }

    @Override
    public boolean eliminar(int id) {
        List<Estudiante> estudiantes = listar();
        boolean eliminado = estudiantes.removeIf(e -> e.getId() == id);
        if (eliminado) {
            escribirTodos(estudiantes);
        }
        return eliminado;
    }

    private void escribirTodos(List<Estudiante> estudiantes) {
        try {
            mapper.writeValue(archivo, estudiantes);
        } catch (IOException e) {
            throw new UncheckedIOException("No fue posible escribir el archivo JSON.", e);
        }
    }
}
