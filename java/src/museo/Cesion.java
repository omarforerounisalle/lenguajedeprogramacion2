package museo;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Cesion {

    private static final AtomicInteger ID_SEQ = new AtomicInteger(0);

    private final int idCesion;
    private final ObraArte obra;
    private final MuseoColaborador museo;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final double importePagado;
    private String estado;

    public Cesion(
            ObraArte obra,
            MuseoColaborador museo,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            double importePagado,
            String estado
    ) {
        this.idCesion = ID_SEQ.incrementAndGet();
        this.obra = obra;
        this.museo = museo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.importePagado = importePagado;
        this.estado = estado;
    }

    public int getIdCesion() {
        return idCesion;
    }

    public ObraArte getObra() {
        return obra;
    }

    public MuseoColaborador getMuseo() {
        return museo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public double getImportePagado() {
        return importePagado;
    }

    public String getEstado() {
        return estado;
    }

    public void iniciar() {
        obra.estado = EstadosMuseo.CEDIDA;
        obra.cesionActiva = this;
        this.estado = "activa";
    }

    public void finalizar() {
        this.estado = "finalizada";
        if (obra.cesionActiva == this) {
            obra.cesionActiva = null;
        }
        obra.marcarExpuesta();
    }

    public boolean estaActiva() {
        return "activa".equals(estado);
    }
}
