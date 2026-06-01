using CrudMvcDao.Model;

namespace CrudMvcDao.Dao;

/// <summary>
/// Patrón DAO (Data Access Object).
///
/// Contrato que abstrae las operaciones de persistencia sobre Estudiante.
///
/// SOLID:
///  - DIP: el controlador depende de esta abstracción, no de la implementación
///    concreta (JSON, BD, memoria, etc.).
///  - OCP: se pueden agregar nuevas implementaciones (SQL Server, SQLite, XML…)
///    sin modificar el código que ya consume este contrato.
///  - ISP: la interfaz expone solo operaciones CRUD, sin métodos forzados.
/// </summary>
public interface IEstudianteDao
{
    Estudiante Crear(Estudiante estudiante);

    IReadOnlyList<Estudiante> Listar();

    Estudiante? BuscarPorId(int id);

    Estudiante Actualizar(Estudiante estudiante);

    bool Eliminar(int id);
}
