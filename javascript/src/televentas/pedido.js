export class Pedido {
  /**
   * @param {number} idPedido
   * @param {string} estado
   * @param {Date} fechaPreparacion
   * @param {boolean} empaquetado
   * @param {import('./ordenCompra.js').OrdenCompra} orden
   */
  constructor(idPedido, estado, fechaPreparacion, empaquetado, orden) {
    this.idPedido = idPedido;
    this.estado = estado;
    this.fechaPreparacion = fechaPreparacion;
    this.empaquetado = empaquetado;
    this.orden = orden;
  }

  getIdPedido() {
    return this.idPedido;
  }

  getEstado() {
    return this.estado;
  }

  getFechaPreparacion() {
    return this.fechaPreparacion;
  }

  isEmpaquetado() {
    return this.empaquetado;
  }

  getOrden() {
    return this.orden;
  }

  preparar() {
    this.estado = "en_preparacion";
  }

  empaquetar() {
    this.empaquetado = true;
    this.estado = "empaquetado";
  }

  marcarListo() {
    if (!this.empaquetado) {
      throw new Error("No se puede marcar como listo un pedido no empaquetado.");
    }
    this.estado = "listo";
  }
}
