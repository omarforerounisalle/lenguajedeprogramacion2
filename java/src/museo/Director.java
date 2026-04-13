package museo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Director extends Usuario {

    private final String cargo;
    private final CatalogoObras catalogo;
    private final ArrayList<Cesion> cesiones = new ArrayList<>();
    private final ArrayList<MuseoColaborador> museosColaboradores = new ArrayList<>();

    public Director(
            int idUsuario,
            String nombre,
            String correo,
            boolean activo,
            String contrasena,
            String cargo,
            CatalogoObras catalogo
    ) {
        super(idUsuario, nombre, correo, activo, contrasena);
        this.cargo = cargo;
        this.catalogo = catalogo;
    }

    public String getCargo() {
        return cargo;
    }

    public List<Cesion> getCesiones() {
        return Collections.unmodifiableList(new ArrayList<>(cesiones));
    }

    public List<MuseoColaborador> getMuseosColaboradores() {
        return Collections.unmodifiableList(new ArrayList<>(museosColaboradores));
    }

    public void registrarMuseoColaborador(MuseoColaborador museo) {
        if (!autenticar()) {
            throw new IllegalStateException("Debe autenticarse.");
        }
        if (!museosColaboradores.contains(museo)) {
            museosColaboradores.add(museo);
        }
    }

    public Optional<Cesion> cederObra(
            ObraArte obra,
            MuseoColaborador museo,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            double importePagado
    ) {
        if (!autenticar()) {
            throw new IllegalStateException("Debe autenticarse.");
        }
        if (EstadosMuseo.CEDIDA.equals(obra.estado)) {
            obra.colaSolicitudesCesion.add(museo);
            museo.solicitarObra();
            return Optional.empty();
        }
        if (!obra.estaDisponibleParaCesion()) {
            throw new IllegalStateException("La obra no esta disponible para cesion.");
        }
        Cesion cesion = new Cesion(obra, museo, fechaInicio, fechaFin, importePagado, "pendiente");
        cesion.iniciar();
        cesiones.add(cesion);
        museo.recibirCesion();
        return Optional.of(cesion);
    }

    public Optional<Cesion> finalizarCesionYAsignarSiguiente(Cesion cesion) {
        if (!autenticar()) {
            throw new IllegalStateException("Debe autenticarse.");
        }
        ObraArte obra = cesion.getObra();
        long duracion = ChronoUnit.DAYS.between(cesion.getFechaInicio(), cesion.getFechaFin());
        cesion.finalizar();
        if (!obra.colaSolicitudesCesion.isEmpty()) {
            MuseoColaborador siguiente = obra.colaSolicitudesCesion.remove(0);
            LocalDate inicio = LocalDate.now();
            LocalDate nuevaFin = inicio.plusDays(duracion);
            return cederObra(obra, siguiente, inicio, nuevaFin, cesion.getImportePagado());
        }
        return Optional.empty();
    }

    public double consultarValorTotal() {
        if (!autenticar()) {
            throw new IllegalStateException("Debe autenticarse.");
        }
        double suma = 0;
        for (ObraArte o : catalogo.listarObras()) {
            suma += o.getValorEconomico();
        }
        return suma;
    }
}
