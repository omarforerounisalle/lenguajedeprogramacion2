package televentas;

import java.time.LocalDate;
import java.util.Optional;

public class Queja {

    private final int idQueja;
    private final LocalDate fecha;
    private final String motivo;
    private final String descripcion;
    private final Optional<OrdenCompra> orden;
    String estado;

    public Queja(int idQueja, LocalDate fecha, String motivo, String descripcion, OrdenCompra orden) {
        this.idQueja = idQueja;
        this.fecha = fecha;
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.orden = Optional.ofNullable(orden);
        this.estado = "registrada";
    }

    public int getIdQueja() {
        return idQueja;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public Optional<OrdenCompra> getOrden() {
        return orden;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void registrar() {
        this.estado = "registrada";
    }

    public void remitir() {
        this.estado = "remitida";
    }

    public void cerrar() {
        this.estado = "cerrada";
    }
}
