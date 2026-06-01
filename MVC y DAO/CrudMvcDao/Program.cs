using CrudMvcDao.Controller;
using CrudMvcDao.Dao;
using CrudMvcDao.View;

// Punto de entrada.
//
// Aplica DIP: se construye la implementación concreta del DAO aquí y se inyecta
// en el controlador, que solo conoce la abstracción IEstudianteDao.

var archivo = Path.Combine(AppContext.BaseDirectory, "estudiantes.json");
IEstudianteDao dao = new EstudianteDaoJson(archivo);
var controller = new EstudianteController(dao);
var view = new EstudianteView();

var continuar = true;
while (continuar)
{
    var opcion = view.MostrarMenu();
    try
    {
        switch (opcion)
        {
            case 1:
            {
                var e = view.LeerEstudiante();
                controller.Crear(e.Id, e.Nombre, e.Apellido, e.Correo, e.Programa);
                view.MostrarMensaje("Estudiante creado correctamente.");
                break;
            }
            case 2:
                view.MostrarEstudiantes(controller.Listar());
                break;
            case 3:
                view.MostrarEstudiante(controller.BuscarPorId(view.LeerId()));
                break;
            case 4:
            {
                var e = view.LeerEstudiante();
                controller.Actualizar(e.Id, e.Nombre, e.Apellido, e.Correo, e.Programa);
                view.MostrarMensaje("Estudiante actualizado correctamente.");
                break;
            }
            case 5:
            {
                var ok = controller.Eliminar(view.LeerId());
                view.MostrarMensaje(ok
                    ? "Estudiante eliminado."
                    : "No existe un estudiante con ese id.");
                break;
            }
            case 0:
                continuar = false;
                view.MostrarMensaje("Hasta luego.");
                break;
            default:
                view.MostrarMensaje("Opción no válida.");
                break;
        }
    }
    catch (Exception ex)
    {
        view.MostrarMensaje($"Error: {ex.Message}");
    }
}
