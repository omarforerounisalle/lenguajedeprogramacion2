export class Queja {
  /**
   * @param {number} idQueja
   * @param {Date} fecha
   * @param {string} motivo
   * @param {string} descripcion
   * @param {import('./ordenCompra.js').OrdenCompra | null} orden
   */
  constructor(idQueja, fecha, motivo, descripcion, orden) {
    this.idQueja = idQueja;
    this.fecha = fecha;
    this.motivo = motivo;
    this.descripcion = descripcion;
    /** @type {import('./ordenCompra.js').OrdenCompra | null} */
    this.orden = orden ?? null;
    this.estado = "registrada";
  }

  getIdQueja() {
    return this.idQueja;
  }

  getFecha() {
    return this.fecha;
  }

  getMotivo() {
    return this.motivo;
  }

  getDescripcion() {
    return this.descripcion;
  }

  getEstado() {
    return this.estado;
  }

  /** @returns {import('./ordenCompra.js').OrdenCompra | null} */
  getOrden() {
    return this.orden;
  }

  setEstado(estado) {
    this.estado = estado;
  }

  registrar() {
    this.estado = "registrada";
  }

  remitir() {
    this.estado = "remitida";
  }

  cerrar() {
    this.estado = "cerrada";
  }
}
