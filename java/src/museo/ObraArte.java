package museo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class ObraArte {

    private final int idObra;
    private final String titulo;
    private final String autor;
    private final String periodo;
    private final double valorEconomico;
    private final LocalDate fechaCreacion;
    private final LocalDate fechaIngreso;

    /** Visible en paquete para Cesion, Director y subclases. */
    String estado;

    Sala sala;
    Cesion cesionActiva;
    final ArrayList<MuseoColaborador> colaSolicitudesCesion = new ArrayList<>();
    private final ArrayList<Restauracion> restauraciones = new ArrayList<>();

    protected ObraArte(
            int idObra,
            String titulo,
            String autor,
            String periodo,
            double valorEconomico,
            LocalDate fechaCreacion,
            LocalDate fechaIngreso,
            String estado
    ) {
        this.idObra = idObra;
        this.titulo = titulo;
        this.autor = autor;
        this.periodo = periodo;
        this.valorEconomico = valorEconomico;
        this.fechaCreacion = fechaCreacion;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    public int getIdObra() {
        return idObra;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getPeriodo() {
        return periodo;
    }

    public double getValorEconomico() {
        return valorEconomico;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public String getEstado() {
        return estado;
    }

    public void enviarARestauracion() {
        this.estado = EstadosMuseo.RESTAURACION;
    }

    public void marcarExpuesta() {
        this.estado = EstadosMuseo.EXPUESTA;
    }

    public boolean estaDisponibleParaCesion() {
        if (EstadosMuseo.RESTAURACION.equals(estado)) {
            return false;
        }
        if (EstadosMuseo.CEDIDA.equals(estado)) {
            return false;
        }
        if (EstadosMuseo.DANADA.equals(estado)) {
            return false;
        }
        return EstadosMuseo.EXPUESTA.equals(estado);
    }

    public int calcularAntiguedad() {
        LocalDate hoy = LocalDate.now();
        return (int) Math.max(0, ChronoUnit.YEARS.between(fechaCreacion, hoy));
    }

    public void registrarRestauracion(Restauracion r) {
        restauraciones.add(r);
    }

    public List<Restauracion> restauracionesOrdenadasPorAntiguedad() {
        ArrayList<Restauracion> copia = new ArrayList<>(restauraciones);
        copia.sort(Comparator.comparing(Restauracion::getFechaInicio));
        return Collections.unmodifiableList(copia);
    }

    public LocalDate fechaReferenciaProximoCicloRestauracion() {
        return restauraciones.stream()
                .map(Restauracion::getFechaFin)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(Comparator.naturalOrder())
                .orElse(fechaIngreso);
    }

    public boolean necesitaRestauracionCiclo(LocalDate diaConsulta) {
        if (EstadosMuseo.RESTAURACION.equals(estado) || EstadosMuseo.CEDIDA.equals(estado)) {
            return false;
        }
        LocalDate ref = fechaReferenciaProximoCicloRestauracion();
        LocalDate limite = FechasUtil.agregarAnios(ref, EstadosMuseo.ANIOS_CICLO_RESTAURACION);
        return !diaConsulta.isBefore(limite);
    }

    public abstract String mostrarDetalle();
}
