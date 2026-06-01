export class Producto {
  constructor(codigo, nombre, descripcion, precio, cantidadDisponible) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.precio = precio;
    this.cantidadDisponible = cantidadDisponible;
  }

  getCodigo() {
    return this.codigo;
  }

  getNombre() {
    return this.nombre;
  }

  getDescripcion() {
    return this.descripcion;
  }

  getPrecio() {
    return this.precio;
  }

  getCantidadDisponible() {
    return this.cantidadDisponible;
  }

  estaDisponible(cantidad) {
    return this.cantidadDisponible >= cantidad;
  }

  actualizarPrecio(nuevoPrecio) {
    if (nuevoPrecio <= 0) {
      throw new Error("El nuevo precio debe ser mayor que cero.");
    }
    this.precio = nuevoPrecio;
  }

  actualizarStock(cantidad) {
    if (cantidad < 0) {
      throw new Error("La cantidad disponible no puede ser negativa.");
    }
    this.cantidadDisponible = cantidad;
  }
}
