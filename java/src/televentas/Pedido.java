package televentas;

import java.time.LocalDate;

public class Pedido {

    private final int idPedido;
    private String estado;
    private final LocalDate fechaPreparacion;
    private boolean empaquetado;
    private final OrdenCompra orden;

    public Pedido(int idPedido, String estado, LocalDate fechaPreparacion, boolean empaquetado, OrdenCompra orden) {
        this.idPedido = idPedido;
        this.estado = estado;
        this.fechaPreparacion = fechaPreparacion;
        this.empaquetado = empaquetado;
        this.orden = orden;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getFechaPreparacion() {
        return fechaPreparacion;
    }

    public boolean isEmpaquetado() {
        return empaquetado;
    }

    public OrdenCompra getOrden() {
        return orden;
    }

    public void preparar() {
        this.estado = "en_preparacion";
    }

    public void empaquetar() {
        this.empaquetado = true;
        this.estado = "empaquetado";
    }

    public void marcarListo() {
        if (!empaquetado) {
            throw new IllegalStateException("No se puede marcar como listo un pedido no empaquetado.");
        }
        this.estado = "listo";
    }
}
