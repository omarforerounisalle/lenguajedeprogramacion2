package co.edu.unisalle.crud.controller;

import co.edu.unisalle.crud.dao.IEstudianteDAO;
import co.edu.unisalle.crud.model.Estudiante;

import java.util.List;
import java.util.Optional;

/**
 * Controlador del patrón MVC.
 *
 * SRP: media entre la vista y el DAO sin contener reglas de persistencia ni
 * lógica de presentación.
 * DIP: depende de la abstracción {@link IEstudianteDAO}, no de su
 * implementación concreta — la implementación se inyecta por constructor.
 */
public class EstudianteController {

    private final IEstudianteDAO dao;

    public EstudianteController(IEstudianteDAO dao) {
        this.dao = dao;
    }

    public Estudiante crear(int id, String nombre, String apellido, String correo, String programa) {
        Estudiante estudiante = new Estudiante(id, nombre, apellido, correo, programa);
        return dao.crear(estudiante);
    }

    public List<Estudiante> listar() {
        return dao.listar();
    }

    public Optional<Estudiante> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public Estudiante actualizar(int id, String nombre, String apellido, String correo, String programa) {
        Estudiante estudiante = new Estudiante(id, nombre, apellido, correo, programa);
        return dao.actualizar(estudiante);
    }

    public boolean eliminar(int id) {
        return dao.eliminar(id);
    }
}
