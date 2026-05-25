package co.edu.unisalle.crud;

import co.edu.unisalle.crud.controller.EstudianteController;
import co.edu.unisalle.crud.dao.EstudianteDAOJson;
import co.edu.unisalle.crud.dao.IEstudianteDAO;
import co.edu.unisalle.crud.model.Estudiante;
import co.edu.unisalle.crud.view.EstudianteView;

import java.io.File;
import java.util.Optional;

/**
 * Punto de entrada.
 *
 * Aplica DIP: se construye la implementación concreta del DAO aquí y se inyecta
 * en el controlador, que solo conoce la abstracción IEstudianteDAO.
 */
public class Main {

    public static void main(String[] args) {
        File archivo = new File("data/estudiantes.json");
        IEstudianteDAO dao = new EstudianteDAOJson(archivo);
        EstudianteController controller = new EstudianteController(dao);
        EstudianteView view = new EstudianteView();

        boolean continuar = true;
        while (continuar) {
            int opcion = view.mostrarMenu();
            try {
                switch (opcion) {
                    case 1 -> crear(controller, view);
                    case 2 -> view.mostrarEstudiantes(controller.listar());
                    case 3 -> buscar(controller, view);
                    case 4 -> actualizar(controller, view);
                    case 5 -> eliminar(controller, view);
                    case 0 -> {
                        continuar = false;
                        view.mostrarMensaje("Hasta luego.");
                    }
                    default -> view.mostrarMensaje("Opción no válida.");
                }
            } catch (RuntimeException ex) {
                view.mostrarMensaje("Error: " + ex.getMessage());
            }
        }
    }

    private static void crear(EstudianteController controller, EstudianteView view) {
        Estudiante e = view.leerEstudiante();
        controller.crear(e.getId(), e.getNombre(), e.getApellido(), e.getCorreo(), e.getPrograma());
        view.mostrarMensaje("Estudiante creado correctamente.");
    }

    private static void buscar(EstudianteController controller, EstudianteView view) {
        int id = view.leerId();
        Optional<Estudiante> resultado = controller.buscarPorId(id);
        view.mostrarEstudiante(resultado);
    }

    private static void actualizar(EstudianteController controller, EstudianteView view) {
        Estudiante e = view.leerEstudiante();
        controller.actualizar(e.getId(), e.getNombre(), e.getApellido(), e.getCorreo(), e.getPrograma());
        view.mostrarMensaje("Estudiante actualizado correctamente.");
    }

    private static void eliminar(EstudianteController controller, EstudianteView view) {
        int id = view.leerId();
        boolean ok = controller.eliminar(id);
        view.mostrarMensaje(ok ? "Estudiante eliminado." : "No existe un estudiante con ese id.");
    }
}
