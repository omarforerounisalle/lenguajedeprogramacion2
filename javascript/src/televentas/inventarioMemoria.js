import { InventarioService } from "./inventarioService.js";

export class InventarioMemoria extends InventarioService {
  /**
   * @param {import('./producto.js').Producto[]} productosIniciales
   */
  constructor(productosIniciales) {
    super();
    /** @type {Map<string, import('./producto.js').Producto>} */
    this.productos = new Map();
    for (const producto of productosIniciales) {
      this.productos.set(producto.getCodigo(), producto);
    }
  }

  /** @returns {import('./producto.js').Producto | null} */
  obtenerProducto(codigo) {
    return this.productos.get(codigo) ?? null;
  }

  consultarDisponibilidad(codigo) {
    const p = this.obtenerProducto(codigo);
    if (p == null) {
      return 0;
    }
    return p.getCantidadDisponible();
  }

  actualizarStock(codigo, cantidad) {
    const producto = this.productos.get(codigo);
    if (producto == null) {
      throw new Error(`Producto con codigo ${codigo} no encontrado.`);
    }
    producto.actualizarStock(cantidad);
  }
}
