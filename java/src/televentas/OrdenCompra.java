package televentas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OrdenCompra {

    private final int idOrden;
    private final LocalDate fechaCreacion;
    private final Cliente cliente;
    private String estado;
    private double total;
    private final ArrayList<DetalleOrden> detalles = new ArrayList<>();
    private Pago pago;

    public OrdenCompra(int idOrden, LocalDate fechaCreacion, Cliente cliente) {
        this.idOrden = idOrden;
        this.fechaCreacion = fechaCreacion;
        this.cliente = cliente;
        this.estado = "pendiente";
        this.total = 0.0;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getEstado() {
        return estado;
    }

    public double getTotal() {
        return total;
    }

    public List<DetalleOrden> getDetalles() {
        return Collections.unmodifiableList(new ArrayList<>(detalles));
    }

    public Optional<Pago> getPago() {
        return Optional.ofNullable(pago);
    }

    public void agregarDetalle(DetalleOrden detalle) {
        if (!"pendiente".equals(estado)) {
            throw new IllegalStateException("Solo se pueden agregar detalles a una orden pendiente.");
        }
        detalles.add(detalle);
        this.total = calcularTotal();
    }

    public double calcularTotal() {
        double suma = 0;
        for (DetalleOrden d : detalles) {
            suma += d.calcularSubtotal();
        }
        return suma;
    }

    public void asignarPago(Pago pago) {
        this.pago = pago;
    }

    public void confirmar(InventarioService inventarioService) {
        if (detalles.isEmpty()) {
            throw new IllegalStateException("No se puede confirmar una orden sin productos.");
        }
        if (pago == null) {
            throw new IllegalStateException("No se puede confirmar una orden sin pago asignado.");
        }
        if (!pago.procesar()) {
            throw new IllegalStateException("El pago no pudo ser procesado.");
        }

        for (DetalleOrden detalle : detalles) {
            int disponible = inventarioService.consultarDisponibilidad(detalle.getProducto().getCodigo());
            if (disponible < detalle.getCantidad()) {
                throw new IllegalStateException(
                        "No hay suficiente stock para el producto " + detalle.getProducto().getNombre() + "."
                );
            }
        }

        for (DetalleOrden detalle : detalles) {
            int disponible = inventarioService.consultarDisponibilidad(detalle.getProducto().getCodigo());
            inventarioService.actualizarStock(
                    detalle.getProducto().getCodigo(),
                    disponible - detalle.getCantidad()
            );
        }

        this.estado = "confirmada";
        this.total = calcularTotal();
    }

    public void cancelar() {
        if ("cancelada".equals(estado)) {
            throw new IllegalStateException("La orden ya esta cancelada.");
        }
        if ("despachada".equals(estado)) {
            throw new IllegalStateException("No se puede cancelar una orden ya despachada.");
        }
        this.estado = "cancelada";
    }
}
