package televentas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Demostracion en consola alineada con {@code python/teleVentas.py} (main).
 */
public final class TeleVentasDemo {

    private TeleVentasDemo() {
    }

    public static void main(String[] args) {
        Producto producto1 = new Producto("P001", "Portatil", "Portatil de 14 pulgadas", 2_500_000.0, 10);
        Producto producto2 = new Producto("P002", "Mouse", "Mouse inalambrico", 80_000.0, 25);
        Producto producto3 = new Producto("P003", "Teclado", "Teclado mecanico", 180_000.0, 15);

        Catalogo catalogo = new Catalogo(Arrays.asList(producto1, producto2, producto3));
        InventarioMemoria inventario = new InventarioMemoria(catalogo.listarProductos());
        NotificadorEmailConsola notificador = new NotificadorEmailConsola();

        Cliente cliente = new Cliente(
                1,
                "Carlos Gomez",
                "carlos@email.com",
                "Calle 10 # 20-30, Floridablanca",
                "3001234567"
        );

        AgenteDeposito agente = new AgenteDeposito(
                2,
                "Laura Perez",
                "laura@televendas.com",
                "AD-001"
        );

        GerenteRelaciones gerente = new GerenteRelaciones(
                3,
                "Marta Rodriguez",
                "marta@televendas.com",
                "Relaciones"
        );

        EmpresaTransporte empresa1 = new EmpresaTransporte(1, "Envios Nacionales", "Nacional");
        ServicioLogistica servicioLogistica = new ServicioLogistica(
                Collections.singletonList(empresa1)
        );

        mostrarCatalogo(catalogo);

        SuscripcionCatalogo suscripcion = cliente.solicitarCatalogo(1, "semanal");
        suscripcion.enviarCatalogo(catalogo, notificador);

        OrdenCompra orden = cliente.crearOrden(1001);

        DetalleOrden detalle1 = new DetalleOrden(1, producto1.getPrecio(), producto1);
        DetalleOrden detalle2 = new DetalleOrden(2, producto2.getPrecio(), producto2);

        orden.agregarDetalle(detalle1);
        orden.agregarDetalle(detalle2);

        PagoTarjetaCredito pago = new PagoTarjetaCredito(
                orden.calcularTotal(),
                LocalDate.now(),
                "****1234",
                "Carlos Gomez",
                "Visa",
                "12/28"
        );
        orden.asignarPago(pago);
        orden.confirmar(inventario);

        System.out.println();
        System.out.println("=== ORDEN CONFIRMADA ===");
        System.out.println("Orden #" + orden.getIdOrden() + " | Estado: " + orden.getEstado()
                + " | Total: $" + String.format("%,.0f", orden.getTotal()));

        List<OrdenCompra> ordenesConfirmadas = agente.consultarOrdenesConfirmadas(
                new ArrayList<>(cliente.getOrdenes())
        );
        Pedido pedido = agente.armarPedido(ordenesConfirmadas.get(0));
        agente.empaquetarPedido(pedido);

        System.out.println();
        System.out.println("=== PEDIDO PREPARADO ===");
        System.out.println("Pedido #" + pedido.getIdPedido() + " | Estado: " + pedido.getEstado());

        Envio envio = agente.coordinarEntrega(pedido, servicioLogistica);
        envio.despachar();

        System.out.println();
        System.out.println("=== ENVIO GENERADO ===");
        String fechaSalida = envio.getFechaSalida().isPresent()
                ? envio.getFechaSalida().get().toString()
                : "null";
        System.out.println("Envio #" + envio.getIdEnvio() + " | Estado: " + envio.getEstado()
                + " | Fecha salida: " + fechaSalida);

        Queja queja = cliente.presentarQueja(
                5001,
                "Demora en la entrega",
                "El pedido tardo mas de lo esperado.",
                orden
        );
        gerente.recibirQueja(queja);
        gerente.gestionarQueja(queja);
        gerente.cerrarQueja(queja);

        System.out.println();
        System.out.println("=== QUEJA GESTIONADA ===");
        System.out.println("Queja #" + queja.getIdQueja() + " | Estado: " + queja.getEstado()
                + " | Motivo: " + queja.getMotivo());

        System.out.println();
        System.out.println("=== INVENTARIO ACTUALIZADO ===");
        mostrarCatalogo(catalogo);
    }

    private static void mostrarCatalogo(Catalogo catalogo) {
        System.out.println();
        System.out.println("=== CATALOGO DE PRODUCTOS ===");
        for (Producto producto : catalogo.listarProductos()) {
            System.out.println(
                    "Codigo: " + producto.getCodigo() + " | "
                            + "Nombre: " + producto.getNombre() + " | "
                            + "Precio: $" + String.format("%,.0f", producto.getPrecio()) + " | "
                            + "Stock: " + producto.getCantidadDisponible()
            );
        }
    }
}
