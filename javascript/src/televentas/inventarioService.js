export class InventarioService {
  constructor() {
    if (new.target === InventarioService) {
      throw new TypeError("No se puede instanciar la clase abstracta InventarioService");
    }
  }

  obtenerProducto(_codigo) {
    throw new TypeError("Metodo abstracto");
  }

  consultarDisponibilidad(_codigo) {
    throw new TypeError("Metodo abstracto");
  }

  actualizarStock(_codigo, _cantidad) {
    throw new TypeError("Metodo abstracto");
  }
}
