import { Usuario } from "./usuario.js";
import { hoyUTC } from "./fechasUtil.js";
import { OrdenCompra } from "./ordenCompra.js";
import { SuscripcionCatalogo } from "./suscripcionCatalogo.js";
import { Queja } from "./queja.js";

export class Cliente extends Usuario {
  constructor(idUsuario, nombre, correo, direccionEnvio, telefono, activo = true) {
    super(idUsuario, nombre, correo, activo);
    this.direccionEnvio = direccionEnvio;
    this.telefono = telefono;
    /** @type {OrdenCompra[]} */
    this.ordenes = [];
    /** @type {Queja[]} */
    this.quejas = [];
    /** @type {SuscripcionCatalogo[]} */
    this.suscripciones = [];
  }

  getDireccionEnvio() {
    return this.direccionEnvio;
  }

  getTelefono() {
    return this.telefono;
  }

  getOrdenes() {
    return [...this.ordenes];
  }

  getQuejas() {
    return [...this.quejas];
  }

  getSuscripciones() {
    return [...this.suscripciones];
  }

  consultarCatalogo(catalogo) {
    return catalogo.listarProductos();
  }

  solicitarCatalogo(idSuscripcion, frecuencia) {
    const suscripcion = new SuscripcionCatalogo(idSuscripcion, this.getCorreo(), frecuencia, true);
    this.suscripciones.push(suscripcion);
    return suscripcion;
  }

  crearOrden(idOrden) {
    const orden = new OrdenCompra(idOrden, hoyUTC(), this);
    this.ordenes.push(orden);
    return orden;
  }

  presentarQueja(idQueja, motivo, descripcion, orden) {
    const queja = new Queja(idQueja, hoyUTC(), motivo, descripcion, orden);
    this.quejas.push(queja);
    return queja;
  }

  cancelarOrden(orden) {
    if (!this.ordenes.includes(orden)) {
      throw new Error("La orden no pertenece a este cliente.");
    }
    orden.cancelar();
  }
}
