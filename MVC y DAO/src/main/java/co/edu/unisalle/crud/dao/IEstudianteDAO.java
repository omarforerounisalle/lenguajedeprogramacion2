package co.edu.unisalle.crud.dao;

import co.edu.unisalle.crud.model.Estudiante;

import java.util.List;
import java.util.Optional;

/**
 * Patrón DAO (Data Access Object).
 *
 * Contrato que abstrae las operaciones de persistencia sobre Estudiante.
 *
 * SOLID:
 *  - DIP: el controlador depende de esta abstracción, no de la implementación
 *    concreta (JSON, BD, memoria, etc.).
 *  - OCP: se pueden agregar nuevas implementaciones (MySQL, SQLite, XML…) sin
 *    modificar el código que ya consume este contrato.
 *  - ISP: la interfaz expone solo operaciones CRUD, sin métodos forzados.
 */
public interface IEstudianteDAO {

    Estudiante crear(Estudiante estudiante);

    List<Estudiante> listar();

    Optional<Estudiante> buscarPorId(int id);

    Estudiante actualizar(Estudiante estudiante);

    boolean eliminar(int id);
}
