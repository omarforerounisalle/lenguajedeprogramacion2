using CrudMvcDao.Dao;
using CrudMvcDao.Model;

namespace CrudMvcDao.Controller;

/// <summary>
/// Controlador del patrón MVC.
///
/// SRP: media entre la vista y el DAO sin contener reglas de persistencia ni
/// lógica de presentación.
/// DIP: depende de la abstracción <see cref="IEstudianteDao"/>, no de su
/// implementación concreta — la implementación se inyecta por constructor.
/// </summary>
public class EstudianteController
{
    private readonly IEstudianteDao _dao;

    public EstudianteController(IEstudianteDao dao)
    {
        _dao = dao;
    }

    public Estudiante Crear(int id, string nombre, string apellido, string correo, string programa) =>
        _dao.Crear(new Estudiante(id, nombre, apellido, correo, programa));

    public IReadOnlyList<Estudiante> Listar() => _dao.Listar();

    public Estudiante? BuscarPorId(int id) => _dao.BuscarPorId(id);

    public Estudiante Actualizar(int id, string nombre, string apellido, string correo, string programa) =>
        _dao.Actualizar(new Estudiante(id, nombre, apellido, correo, programa));

    public bool Eliminar(int id) => _dao.Eliminar(id);
}
