package museo;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Restauracion {

    private static final AtomicInteger ID_SEQ = new AtomicInteger(0);

    private final int idRestauracion;
    private final ObraArte obra;
    private final String tipoRestauracion;
    private final LocalDate fechaInicio;
    private LocalDate fechaFin;
    private final String motivo;

    public Restauracion(
            ObraArte obra,
            String tipoRestauracion,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            String motivo
    ) {
        this.idRestauracion = ID_SEQ.incrementAndGet();
        this.obra = obra;
        this.tipoRestauracion = tipoRestauracion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
    }

    public int getIdRestauracion() {
        return idRestauracion;
    }

    public ObraArte getObra() {
        return obra;
    }

    public String getTipoRestauracion() {
        return tipoRestauracion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public Optional<LocalDate> getFechaFin() {
        return Optional.ofNullable(fechaFin);
    }

    public String getMotivo() {
        return motivo;
    }

    public void iniciar() {
        obra.enviarARestauracion();
    }

    public void finalizar() {
        this.fechaFin = LocalDate.now();
        obra.marcarExpuesta();
    }

    public boolean estaActiva() {
        return fechaFin == null;
    }
}
