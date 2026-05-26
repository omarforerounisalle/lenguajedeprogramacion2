namespace SistemaAcademico.Patterns.Facade;

/// <summary>
/// Patrón Facade.
///
/// Expone una operación de alto nivel — <see cref="Matricular"/> — que
/// orquesta varios subsistemas (cupos, pagos, registro y notificaciones)
/// detrás de una sola llamada, ocultando su complejidad al cliente.
/// </summary>
public class MatriculaFacade
{
    private readonly ServicioCupos _cupos;
    private readonly ServicioPagos _pagos;
    private readonly ServicioRegistro _registro;
    private readonly ServicioNotificaciones _notificaciones;

    public MatriculaFacade(
        ServicioCupos cupos,
        ServicioPagos pagos,
        ServicioRegistro registro,
        ServicioNotificaciones notificaciones)
    {
        _cupos = cupos;
        _pagos = pagos;
        _registro = registro;
        _notificaciones = notificaciones;
    }

    public bool Matricular(int estudianteId, string codigoCurso)
    {
        if (!_pagos.EstaAlDia(estudianteId))
        {
            _notificaciones.Notificar(estudianteId, $"No se puede matricular {codigoCurso}: pagos pendientes.");
            return false;
        }

        if (!_cupos.HayCupo(codigoCurso))
        {
            _notificaciones.Notificar(estudianteId, $"No se puede matricular {codigoCurso}: sin cupos.");
            return false;
        }

        _cupos.ReservarCupo(codigoCurso);
        _registro.RegistrarMatricula(estudianteId, codigoCurso);
        _notificaciones.Notificar(estudianteId, $"Matrícula confirmada en {codigoCurso}.");
        return true;
    }
}
