package televentas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AgenteDeposito extends Usuario {

    private final String codigoEmpleado;

    public AgenteDeposito(int idUsuario, String nombre, String correo, String codigoEmpleado) {
        super(idUsuario, nombre, correo, true);
        this.codigoEmpleado = codigoEmpleado;
    }

    public AgenteDeposito(int idUsuario, String nombre, String correo, String codigoEmpleado, boolean activo) {
        super(idUsuario, nombre, correo, activo);
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public List<OrdenCompra> consultarOrdenesConfirmadas(List<OrdenCompra> ordenes) {
        ArrayList<OrdenCompra> out = new ArrayList<>();
        for (OrdenCompra orden : ordenes) {
            if ("confirmada".equals(orden.getEstado())) {
                out.add(orden);
            }
        }
        return out;
    }

    public Pedido armarPedido(OrdenCompra orden) {
        if (!"confirmada".equals(orden.getEstado())) {
            throw new IllegalStateException("Solo se puede armar un pedido a partir de una orden confirmada.");
        }
        return new Pedido(
                orden.getIdOrden(),
                "pendiente",
                LocalDate.now(),
                false,
                orden
        );
    }

    public void empaquetarPedido(Pedido pedido) {
        pedido.preparar();
        pedido.empaquetar();
        pedido.marcarListo();
    }

    public Envio coordinarEntrega(Pedido pedido, ServicioLogistica servicioLogistica) {
        return servicioLogistica.generarEnvio(pedido);
    }
}
