package co.edu.unisalle.crud;

import co.edu.unisalle.crud.controller.EstudianteController;
import co.edu.unisalle.crud.dao.EstudianteDAOJson;
import co.edu.unisalle.crud.dao.IEstudianteDAO;
import co.edu.unisalle.crud.model.Estudiante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias sobre el DAO JSON y el Controller.
 *
 * Se usa @TempDir para aislar cada prueba en un archivo JSON propio,
 * garantizando independencia entre casos.
 */
class EstudianteDAOJsonTest {

    @TempDir
    Path tempDir;

    private File archivo;
    private IEstudianteDAO dao;
    private EstudianteController controller;

    @BeforeEach
    void setUp() {
        archivo = tempDir.resolve("estudiantes.json").toFile();
        dao = new EstudianteDAOJson(archivo);
        controller = new EstudianteController(dao);
    }

    @Test
    @DisplayName("01 - El archivo JSON se crea al instanciar el DAO")
    void elArchivoSeCreaAlInstanciarElDao() {
        assertTrue(archivo.exists(), "El archivo JSON debería existir tras instanciar el DAO.");
    }

    @Test
    @DisplayName("02 - Listar inicialmente devuelve una lista vacía")
    void listarInicialmenteRetornaListaVacia() {
        List<Estudiante> lista = dao.listar();
        assertNotNull(lista);
        assertTrue(lista.isEmpty(), "La lista inicial debe estar vacía.");
    }

    @Test
    @DisplayName("03 - Crear un estudiante lo agrega correctamente")
    void crearEstudianteAgregaCorrectamente() {
        Estudiante creado = dao.crear(new Estudiante(1, "Ana", "Pérez", "ana@unisalle.edu.co", "Ing. Sistemas"));
        assertNotNull(creado);
        assertEquals(1, dao.listar().size());
        assertEquals("Ana", creado.getNombre());
    }

    @Test
    @DisplayName("04 - Crear con id duplicado lanza IllegalArgumentException")
    void crearConIdDuplicadoLanzaExcepcion() {
        dao.crear(new Estudiante(1, "Ana", "Pérez", "ana@unisalle.edu.co", "Ing. Sistemas"));
        assertThrows(IllegalArgumentException.class,
                () -> dao.crear(new Estudiante(1, "Otro", "Apellido", "otro@unisalle.edu.co", "Ing. Industrial")));
    }

    @Test
    @DisplayName("05 - Buscar por id existente devuelve el estudiante")
    void buscarPorIdExistenteDevuelveElEstudiante() {
        dao.crear(new Estudiante(5, "Luis", "Gómez", "luis@unisalle.edu.co", "Ing. Sistemas"));
        Optional<Estudiante> encontrado = dao.buscarPorId(5);
        assertTrue(encontrado.isPresent());
        assertEquals("Luis", encontrado.get().getNombre());
    }

    @Test
    @DisplayName("06 - Buscar por id inexistente devuelve Optional vacío")
    void buscarPorIdInexistenteDevuelveOptionalVacio() {
        Optional<Estudiante> encontrado = dao.buscarPorId(999);
        assertFalse(encontrado.isPresent());
    }

    @Test
    @DisplayName("07 - Actualizar estudiante existente modifica sus datos")
    void actualizarEstudianteExistenteModificaDatos() {
        dao.crear(new Estudiante(7, "Carlos", "Ruiz", "carlos@unisalle.edu.co", "Ing. Sistemas"));
        Estudiante actualizado = dao.actualizar(
                new Estudiante(7, "Carlos Andrés", "Ruiz", "carlos.ruiz@unisalle.edu.co", "Ing. Industrial"));
        assertEquals("Carlos Andrés", actualizado.getNombre());
        assertEquals("Ing. Industrial", dao.buscarPorId(7).orElseThrow().getPrograma());
    }

    @Test
    @DisplayName("08 - Actualizar un estudiante inexistente lanza IllegalArgumentException")
    void actualizarInexistenteLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> dao.actualizar(new Estudiante(404, "Nadie", "X", "x@x.com", "X")));
    }

    @Test
    @DisplayName("09 - Eliminar estudiante existente retorna true y reduce la lista")
    void eliminarEstudianteExistente() {
        dao.crear(new Estudiante(9, "María", "López", "maria@unisalle.edu.co", "Ing. Sistemas"));
        boolean eliminado = dao.eliminar(9);
        assertTrue(eliminado);
        assertTrue(dao.listar().isEmpty());
    }

    @Test
    @DisplayName("10 - Eliminar un id inexistente retorna false")
    void eliminarInexistenteRetornaFalse() {
        assertFalse(dao.eliminar(123));
    }

    @Test
    @DisplayName("11 - Persistencia: la información sobrevive a una nueva instancia del DAO")
    void persistenciaEnArchivoJson() {
        dao.crear(new Estudiante(11, "Sara", "Mora", "sara@unisalle.edu.co", "Ing. Sistemas"));
        // Nueva instancia leyendo el mismo archivo
        IEstudianteDAO otroDao = new EstudianteDAOJson(archivo);
        List<Estudiante> lista = otroDao.listar();
        assertEquals(1, lista.size());
        assertEquals("Sara", lista.get(0).getNombre());
    }

    @Test
    @DisplayName("12 - Controller integra correctamente con el DAO (flujo CRUD)")
    void controllerCrudIntegraConDao() {
        controller.crear(12, "Diego", "Vargas", "diego@unisalle.edu.co", "Ing. Sistemas");
        assertEquals(1, controller.listar().size());

        controller.actualizar(12, "Diego", "Vargas", "diego.vargas@unisalle.edu.co", "Ing. Industrial");
        assertEquals("diego.vargas@unisalle.edu.co",
                controller.buscarPorId(12).orElseThrow().getCorreo());

        assertTrue(controller.eliminar(12));
        assertTrue(controller.listar().isEmpty());
    }
}
