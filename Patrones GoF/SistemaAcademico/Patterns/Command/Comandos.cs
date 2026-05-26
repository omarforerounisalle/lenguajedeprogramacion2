namespace SistemaAcademico.Patterns.Command;

public class MatricularCommand : IAccionMatricula
{
    private readonly RegistroMatriculas _registro;
    private readonly int _estudianteId;
    private readonly string _codigoCurso;

    public MatricularCommand(RegistroMatriculas registro, int estudianteId, string codigoCurso)
    {
        _registro = registro;
        _estudianteId = estudianteId;
        _codigoCurso = codigoCurso;
    }

    public string Descripcion => $"Matricular({_estudianteId}, {_codigoCurso})";

    public void Ejecutar() => _registro.Matricular(_estudianteId, _codigoCurso);
    public void Deshacer() => _registro.Retirar(_estudianteId, _codigoCurso);
}

public class RetirarCommand : IAccionMatricula
{
    private readonly RegistroMatriculas _registro;
    private readonly int _estudianteId;
    private readonly string _codigoCurso;

    public RetirarCommand(RegistroMatriculas registro, int estudianteId, string codigoCurso)
    {
        _registro = registro;
        _estudianteId = estudianteId;
        _codigoCurso = codigoCurso;
    }

    public string Descripcion => $"Retirar({_estudianteId}, {_codigoCurso})";

    public void Ejecutar() => _registro.Retirar(_estudianteId, _codigoCurso);
    public void Deshacer() => _registro.Matricular(_estudianteId, _codigoCurso);
}
