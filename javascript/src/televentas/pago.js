export class Pago {
  /**
   * @param {number} monto
   * @param {Date} fechaPago
   * @param {string | undefined} estado
   */
  constructor(monto, fechaPago, estado) {
    if (new.target === Pago) {
      throw new TypeError("No se puede instanciar la clase abstracta Pago");
    }
    this.monto = monto;
    this.fechaPago = fechaPago;
    this.estado = estado ?? "pendiente";
  }

  getMonto() {
    return this.monto;
  }

  getFechaPago() {
    return this.fechaPago;
  }

  getEstado() {
    return this.estado;
  }

  procesar() {
    throw new TypeError("Metodo abstracto");
  }

  validar() {
    throw new TypeError("Metodo abstracto");
  }
}
