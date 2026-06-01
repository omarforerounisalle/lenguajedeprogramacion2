package museo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RestauradorJefe extends Usuario {

    private final String especialidad;
    private final ArrayList<Restauracion> restauracionesGestionadas = new ArrayList<>();

    public RestauradorJefe(
            int idUsuario,
            String nombre,
            String correo,
            boolean activo,
            String contrasena,
            String especialidad
    ) {
        super(idUsuario, nombre, correo, activo, contrasena);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public Restauracion iniciarRestauracion(
            ObraArte obra,
            String tipoRestauracion,
            LocalDate fechaInicio,
            String motivo
    ) {
        if (!autenticar()) {
            throw new IllegalStateException("Debe autenticarse.");
        }
        Restauracion r = new Restauracion(obra, tipoRestauracion, fechaInicio, null, motivo);
        r.iniciar();
        obra.registrarRestauracion(r);
        restauracionesGestionadas.add(r);
        return r;
    }

    public void finalizarRestauracion(Restauracion restauracion) {
        if (!autenticar()) {
            throw new IllegalStateException("Debe autenticarse.");
        }
        restauracion.finalizar();
    }

    public List<Restauracion> consultarRestauraciones() {
        if (!autenticar()) {
            throw new IllegalStateException("Debe autenticarse.");
        }
        ArrayList<Restauracion> copia = new ArrayList<>(restauracionesGestionadas);
        copia.sort(Comparator.comparing(Restauracion::getFechaInicio));
        return Collections.unmodifiableList(copia);
    }

    public List<Restauracion> consultarRestauracionesPorObra(ObraArte obra) {
        if (!autenticar()) {
            throw new IllegalStateException("Debe autenticarse.");
        }
        return obra.restauracionesOrdenadasPorAntiguedad();
    }
}
