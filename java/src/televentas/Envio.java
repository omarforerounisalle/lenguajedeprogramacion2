package televentas;

import java.time.LocalDate;
import java.util.Optional;

public class Envio {

    private final int idEnvio;
    private final String direccionDestino;
    private LocalDate fechaSalida;
    private String estado;

    public Envio(int idEnvio, String direccionDestino, LocalDate fechaSalida, String estado) {
        this.idEnvio = idEnvio;
        this.direccionDestino = direccionDestino;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
    }

    public int getIdEnvio() {
        return idEnvio;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public Optional<LocalDate> getFechaSalida() {
        return Optional.ofNullable(fechaSalida);
    }

    public String getEstado() {
        return estado;
    }

    public void programar() {
        this.estado = "programado";
    }

    public void despachar() {
        this.estado = "despachado";
        this.fechaSalida = LocalDate.now();
    }

    public void entregar() {
        this.estado = "entregado";
    }
}
