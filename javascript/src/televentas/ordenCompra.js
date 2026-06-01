export class OrdenCompra {
  /**
   * @param {number} idOrden
   * @param {Date} fechaCreacion
   * @param {import('./cliente.js').Cliente} cliente
   */
  constructor(idOrden, fechaCreacion, cliente) {
    this.idOrden = idOrden;
    this.fechaCreacion = fechaCreacion;
    this.cliente = cliente;
    this.estado = "pendiente";
    this.total = 0.0;
    /** @type {import('./detalleOrden.js').DetalleOrden[]} */
    this.detalles = [];
    /** @type {import('./pago.js').Pago | null} */
    this.pago = null;
  }

  getIdOrden() {
    return this.idOrden;
  }

  getFechaCreacion() {
    return this.fechaCreacion;
  }

  getCliente() {
    return this.cliente;
  }

  getEstado() {
    return this.estado;
  }

  getTotal() {
    return this.total;
  }

  getDetalles() {
    return [...this.detalles];
  }

  /** @returns {import('./pago.js').Pago | null} */
  getPago() {
    return this.pago;
  }

  agregarDetalle(detalle) {
    if (this.estado !== "pendiente") {
      throw new Error("Solo se pueden agregar detalles a una orden pendiente.");
    }
    this.detalles.push(detalle);
    this.total = this.calcularTotal();
  }

  calcularTotal() {
    let suma = 0;
    for (const d of this.detalles) {
      suma += d.calcularSubtotal();
    }
    return suma;
  }

  /** @param {import('./pago.js').Pago} pago */
  asignarPago(pago) {
    this.pago = pago;
  }

  /**
   * @param {{ consultarDisponibilidad: (c: string) => number; actualizarStock: (c: string, n: number) => void }} inventarioService
   */
  confirmar(inventarioService) {
    if (this.detalles.length === 0) {
      throw new Error("No se puede confirmar una orden sin productos.");
    }
    if (this.pago == null) {
      throw new Error("No se puede confirmar una orden sin pago asignado.");
    }
    if (!this.pago.procesar()) {
      throw new Error("El pago no pudo ser procesado.");
    }

    for (const detalle of this.detalles) {
      const disponible = inventarioService.consultarDisponibilidad(detalle.getProducto().getCodigo());
      if (disponible < detalle.getCantidad()) {
        throw new Error(`No hay suficiente stock para el producto ${detalle.getProducto().getNombre()}.`);
      }
    }

    for (const detalle of this.detalles) {
      const disponible = inventarioService.consultarDisponibilidad(detalle.getProducto().getCodigo());
      inventarioService.actualizarStock(detalle.getProducto().getCodigo(), disponible - detalle.getCantidad());
    }

    this.estado = "confirmada";
    this.total = this.calcularTotal();
  }

  cancelar() {
    if (this.estado === "cancelada") {
      throw new Error("La orden ya esta cancelada.");
    }
    if (this.estado === "despachada") {
      throw new Error("No se puede cancelar una orden ya despachada.");
    }
    this.estado = "cancelada";
  }
}
