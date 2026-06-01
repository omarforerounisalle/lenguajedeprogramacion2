import { hoyUTC } from "./fechasUtil.js";

export class Catalogo {
  /**
   * @param {import('./producto.js').Producto[] | undefined} inicial
   */
  constructor(inicial) {
    /** @type {import('./producto.js').Producto[]} */
    this._productos = [];
    this.fechaActualizacion = hoyUTC();
    if (inicial != null && inicial.length > 0) {
      this._productos.push(...inicial);
      this.fechaActualizacion = hoyUTC();
    }
  }

  getFechaActualizacion() {
    return this.fechaActualizacion;
  }

  listarProductos() {
    return [...this._productos];
  }

  /** @returns {import('./producto.js').Producto | null} */
  buscarProducto(codigo) {
    for (const p of this._productos) {
      if (p.getCodigo() === codigo) {
        return p;
      }
    }
    return null;
  }

  agregarProducto(producto) {
    if (this.buscarProducto(producto.getCodigo()) != null) {
      throw new Error(`Ya existe un producto con codigo ${producto.getCodigo()}.`);
    }
    this._productos.push(producto);
    this.fechaActualizacion = hoyUTC();
  }

  eliminarProducto(codigo) {
    const p = this.buscarProducto(codigo);
    if (p == null) {
      throw new Error(`No existe un producto con codigo ${codigo}.`);
    }
    this._productos = this._productos.filter((x) => x.getCodigo() !== codigo);
    this.fechaActualizacion = hoyUTC();
  }
}
