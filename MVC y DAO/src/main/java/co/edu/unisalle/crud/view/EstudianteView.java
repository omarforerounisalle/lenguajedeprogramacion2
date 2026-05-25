package co.edu.unisalle.crud.view;

import co.edu.unisalle.crud.model.Estudiante;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Vista del patrón MVC.
 *
 * SRP: única responsabilidad — interactuar por consola con el usuario.
 * No conoce ni persistencia ni reglas de negocio.
 */
public class EstudianteView {

    private final Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        System.out.println();
        System.out.println("===== CRUD ESTUDIANTES (MVC + DAO) =====");
        System.out.println("1. Crear estudiante");
        System.out.println("2. Listar estudiantes");
        System.out.println("3. Buscar estudiante por id");
        System.out.println("4. Actualizar estudiante");
        System.out.println("5. Eliminar estudiante");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
        return leerEntero();
    }

    public Estudiante leerEstudiante() {
        System.out.print("Id: ");
        int id = leerEntero();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Programa: ");
        String programa = scanner.nextLine();
        return new Estudiante(id, nombre, apellido, correo, programa);
    }

    public int leerId() {
        System.out.print("Id: ");
        return leerEntero();
    }

    public void mostrarEstudiante(Estudiante estudiante) {
        System.out.println(estudiante);
    }

    public void mostrarEstudiante(Optional<Estudiante> estudiante) {
        estudiante.ifPresentOrElse(
                this::mostrarEstudiante,
                () -> mostrarMensaje("No se encontró el estudiante."));
    }

    public void mostrarEstudiantes(List<Estudiante> estudiantes) {
        if (estudiantes.isEmpty()) {
            mostrarMensaje("No hay estudiantes registrados.");
            return;
        }
        estudiantes.forEach(this::mostrarEstudiante);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    private int leerEntero() {
        while (true) {
            String linea = scanner.nextLine();
            try {
                return Integer.parseInt(linea.trim());
            } catch (NumberFormatException ex) {
                System.out.print("Valor inválido, ingrese un número entero: ");
            }
        }
    }
}
