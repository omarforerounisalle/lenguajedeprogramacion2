export class DetalleOrden {
  /**
   * @param {number} cantidad
   * @param {number} precioUnitario
   * @param {import('./producto.js').Producto} producto
   */
  constructor(cantidad, precioUnitario, producto) {
    this.cantidad = cantidad;
    this.precioUnitario = precioUnitario;
    this.producto = producto;
  }

  getCantidad() {
    return this.cantidad;
  }

  getPrecioUnitario() {
    return this.precioUnitario;
  }

  getProducto() {
    return this.producto;
  }

  calcularSubtotal() {
    return this.cantidad * this.precioUnitario;
  }
}
