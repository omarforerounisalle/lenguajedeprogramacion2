import { Usuario } from "./usuario.js";
import { Pedido } from "./pedido.js";
import { hoyUTC } from "./fechasUtil.js";

export class AgenteDeposito extends Usuario {
  constructor(idUsuario, nombre, correo, codigoEmpleado, activo = true) {
    super(idUsuario, nombre, correo, activo);
    this.codigoEmpleado = codigoEmpleado;
  }

  getCodigoEmpleado() {
    return this.codigoEmpleado;
  }

  /** @param {import('./ordenCompra.js').OrdenCompra[]} ordenes */
  consultarOrdenesConfirmadas(ordenes) {
    const out = [];
    for (const orden of ordenes) {
      if (orden.getEstado() === "confirmada") {
        out.push(orden);
      }
    }
    return out;
  }

  /** @param {import('./ordenCompra.js').OrdenCompra} orden */
  armarPedido(orden) {
    if (orden.getEstado() !== "confirmada") {
      throw new Error("Solo se puede armar un pedido a partir de una orden confirmada.");
    }
    return new Pedido(orden.getIdOrden(), "pendiente", hoyUTC(), false, orden);
  }

  /** @param {import('./pedido.js').Pedido} pedido */
  empaquetarPedido(pedido) {
    pedido.preparar();
    pedido.empaquetar();
    pedido.marcarListo();
  }

  /** @param {import('./pedido.js').Pedido} pedido */
  coordinarEntrega(pedido, servicioLogistica) {
    return servicioLogistica.generarEnvio(pedido);
  }
}
