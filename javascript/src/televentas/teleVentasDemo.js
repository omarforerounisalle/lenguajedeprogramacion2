import { Producto } from "./producto.js";
import { Catalogo } from "./catalogo.js";
import { InventarioMemoria } from "./inventarioMemoria.js";
import { NotificadorEmailConsola } from "./notificadorEmailConsola.js";
import { Cliente } from "./cliente.js";
import { AgenteDeposito } from "./agenteDeposito.js";
import { GerenteRelaciones } from "./gerenteRelaciones.js";
import { EmpresaTransporte } from "./empresaTransporte.js";
import { ServicioLogistica } from "./servicioLogistica.js";
import { DetalleOrden } from "./detalleOrden.js";
import { PagoTarjetaCredito } from "./pagoTarjetaCredito.js";
import { hoyUTC } from "./fechasUtil.js";

function fmtMoney(n) {
  return n.toLocaleString("en-US", { maximumFractionDigits: 0 });
}

function mostrarCatalogo(catalogo) {
  console.log();
  console.log("=== CATALOGO DE PRODUCTOS ===");
  for (const producto of catalogo.listarProductos()) {
    console.log(
      `Codigo: ${producto.getCodigo()} | Nombre: ${producto.getNombre()} | Precio: $${fmtMoney(producto.getPrecio())} | Stock: ${producto.getCantidadDisponible()}`
    );
  }
}

function main() {
  const producto1 = new Producto("P001", "Portatil", "Portatil de 14 pulgadas", 2_500_000.0, 10);
  const producto2 = new Producto("P002", "Mouse", "Mouse inalambrico", 80_000.0, 25);
  const producto3 = new Producto("P003", "Teclado", "Teclado mecanico", 180_000.0, 15);

  const catalogo = new Catalogo([producto1, producto2, producto3]);
  const inventario = new InventarioMemoria(catalogo.listarProductos());
  const notificador = new NotificadorEmailConsola();

  const cliente = new Cliente(
    1,
    "Carlos Gomez",
    "carlos@email.com",
    "Calle 10 # 20-30, Floridablanca",
    "3001234567"
  );

  const agente = new AgenteDeposito(2, "Laura Perez", "laura@televendas.com", "AD-001");

  const gerente = new GerenteRelaciones(3, "Marta Rodriguez", "marta@televendas.com", "Relaciones");

  const empresa1 = new EmpresaTransporte(1, "Envios Nacionales", "Nacional");
  const servicioLogistica = new ServicioLogistica([empresa1]);

  mostrarCatalogo(catalogo);

  const suscripcion = cliente.solicitarCatalogo(1, "semanal");
  suscripcion.enviarCatalogo(catalogo, notificador);

  const orden = cliente.crearOrden(1001);

  const detalle1 = new DetalleOrden(1, producto1.getPrecio(), producto1);
  const detalle2 = new DetalleOrden(2, producto2.getPrecio(), producto2);

  orden.agregarDetalle(detalle1);
  orden.agregarDetalle(detalle2);

  const hoy = hoyUTC();
  const pago = new PagoTarjetaCredito(
    orden.calcularTotal(),
    hoy,
    "****1234",
    "Carlos Gomez",
    "Visa",
    "12/28"
  );
  orden.asignarPago(pago);
  orden.confirmar(inventario);

  console.log();
  console.log("=== ORDEN CONFIRMADA ===");
  console.log(`Orden #${orden.getIdOrden()} | Estado: ${orden.getEstado()} | Total: $${fmtMoney(orden.getTotal())}`);

  const ordenesConfirmadas = agente.consultarOrdenesConfirmadas([...cliente.getOrdenes()]);
  const pedido = agente.armarPedido(ordenesConfirmadas[0]);
  agente.empaquetarPedido(pedido);

  console.log();
  console.log("=== PEDIDO PREPARADO ===");
  console.log(`Pedido #${pedido.getIdPedido()} | Estado: ${pedido.getEstado()}`);

  const envio = agente.coordinarEntrega(pedido, servicioLogistica);
  envio.despachar();

  console.log();
  console.log("=== ENVIO GENERADO ===");
  const fechaSalida = envio.getFechaSalida() != null ? envio.getFechaSalida().toISOString().slice(0, 10) : "null";
  console.log(`Envio #${envio.getIdEnvio()} | Estado: ${envio.getEstado()} | Fecha salida: ${fechaSalida}`);

  const queja = cliente.presentarQueja(5001, "Demora en la entrega", "El pedido tardo mas de lo esperado.", orden);
  gerente.recibirQueja(queja);
  gerente.gestionarQueja(queja);
  gerente.cerrarQueja(queja);

  console.log();
  console.log("=== QUEJA GESTIONADA ===");
  console.log(`Queja #${queja.getIdQueja()} | Estado: ${queja.getEstado()} | Motivo: ${queja.getMotivo()}`);

  console.log();
  console.log("=== INVENTARIO ACTUALIZADO ===");
  mostrarCatalogo(catalogo);
}

main();
