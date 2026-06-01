using CrudMvcDao.Controller;
using CrudMvcDao.Dao;
using CrudMvcDao.Model;
using Xunit;

namespace CrudMvcDao.Tests;

/// <summary>
/// Pruebas unitarias sobre el DAO JSON y el Controller.
///
/// Cada prueba usa un archivo temporal único en %TEMP% para garantizar
/// aislamiento entre casos. Se elimina al finalizar mediante IDisposable.
/// </summary>
public class EstudianteDaoJsonTest : IDisposable
{
    private readonly string _archivo;
    private readonly IEstudianteDao _dao;
    private readonly EstudianteController _controller;

    public EstudianteDaoJsonTest()
    {
        _archivo = Path.Combine(Path.GetTempPath(), $"estudiantes_{Guid.NewGuid()}.json");
        _dao = new EstudianteDaoJson(_archivo);
        _controller = new EstudianteController(_dao);
    }

    public void Dispose()
    {
        if (File.Exists(_archivo))
            File.Delete(_archivo);
        GC.SuppressFinalize(this);
    }

    [Fact(DisplayName = "01 - El archivo JSON se crea al instanciar el DAO")]
    public void ElArchivoSeCreaAlInstanciarElDao()
    {
        Assert.True(File.Exists(_archivo), "El archivo JSON debería existir tras instanciar el DAO.");
    }

    [Fact(DisplayName = "02 - Listar inicialmente devuelve una lista vacia")]
    public void ListarInicialmenteRetornaListaVacia()
    {
        var lista = _dao.Listar();
        Assert.NotNull(lista);
        Assert.Empty(lista);
    }

    [Fact(DisplayName = "03 - Crear un estudiante lo agrega correctamente")]
    public void CrearEstudianteAgregaCorrectamente()
    {
        var creado = _dao.Crear(new Estudiante(1, "Ana", "Pérez", "ana@unisalle.edu.co", "Ing. Sistemas"));
        Assert.NotNull(creado);
        Assert.Single(_dao.Listar());
        Assert.Equal("Ana", creado.Nombre);
    }

    [Fact(DisplayName = "04 - Crear con id duplicado lanza ArgumentException")]
    public void CrearConIdDuplicadoLanzaExcepcion()
    {
        _dao.Crear(new Estudiante(1, "Ana", "Pérez", "ana@unisalle.edu.co", "Ing. Sistemas"));
        Assert.Throws<ArgumentException>(() =>
            _dao.Crear(new Estudiante(1, "Otro", "Apellido", "otro@unisalle.edu.co", "Ing. Industrial")));
    }

    [Fact(DisplayName = "05 - Buscar por id existente devuelve el estudiante")]
    public void BuscarPorIdExistenteDevuelveElEstudiante()
    {
        _dao.Crear(new Estudiante(5, "Luis", "Gómez", "luis@unisalle.edu.co", "Ing. Sistemas"));
        var encontrado = _dao.BuscarPorId(5);
        Assert.NotNull(encontrado);
        Assert.Equal("Luis", encontrado!.Nombre);
    }

    [Fact(DisplayName = "06 - Buscar por id inexistente devuelve null")]
    public void BuscarPorIdInexistenteDevuelveNull()
    {
        Assert.Null(_dao.BuscarPorId(999));
    }

    [Fact(DisplayName = "07 - Actualizar estudiante existente modifica sus datos")]
    public void ActualizarEstudianteExistenteModificaDatos()
    {
        _dao.Crear(new Estudiante(7, "Carlos", "Ruiz", "carlos@unisalle.edu.co", "Ing. Sistemas"));
        var actualizado = _dao.Actualizar(
            new Estudiante(7, "Carlos Andrés", "Ruiz", "carlos.ruiz@unisalle.edu.co", "Ing. Industrial"));
        Assert.Equal("Carlos Andrés", actualizado.Nombre);
        Assert.Equal("Ing. Industrial", _dao.BuscarPorId(7)!.Programa);
    }

    [Fact(DisplayName = "08 - Actualizar un estudiante inexistente lanza ArgumentException")]
    public void ActualizarInexistenteLanzaExcepcion()
    {
        Assert.Throws<ArgumentException>(() =>
            _dao.Actualizar(new Estudiante(404, "Nadie", "X", "x@x.com", "X")));
    }

    [Fact(DisplayName = "09 - Eliminar estudiante existente retorna true y reduce la lista")]
    public void EliminarEstudianteExistente()
    {
        _dao.Crear(new Estudiante(9, "María", "López", "maria@unisalle.edu.co", "Ing. Sistemas"));
        Assert.True(_dao.Eliminar(9));
        Assert.Empty(_dao.Listar());
    }

    [Fact(DisplayName = "10 - Eliminar un id inexistente retorna false")]
    public void EliminarInexistenteRetornaFalse()
    {
        Assert.False(_dao.Eliminar(123));
    }

    [Fact(DisplayName = "11 - Persistencia: la información sobrevive a una nueva instancia del DAO")]
    public void PersistenciaEnArchivoJson()
    {
        _dao.Crear(new Estudiante(11, "Sara", "Mora", "sara@unisalle.edu.co", "Ing. Sistemas"));
        IEstudianteDao otroDao = new EstudianteDaoJson(_archivo);
        var lista = otroDao.Listar();
        Assert.Single(lista);
        Assert.Equal("Sara", lista[0].Nombre);
    }

    [Fact(DisplayName = "12 - Controller integra correctamente con el DAO (flujo CRUD)")]
    public void ControllerCrudIntegraConDao()
    {
        _controller.Crear(12, "Diego", "Vargas", "diego@unisalle.edu.co", "Ing. Sistemas");
        Assert.Single(_controller.Listar());

        _controller.Actualizar(12, "Diego", "Vargas", "diego.vargas@unisalle.edu.co", "Ing. Industrial");
        Assert.Equal("diego.vargas@unisalle.edu.co", _controller.BuscarPorId(12)!.Correo);

        Assert.True(_controller.Eliminar(12));
        Assert.Empty(_controller.Listar());
    }
}
