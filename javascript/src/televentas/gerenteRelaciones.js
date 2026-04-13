import { Usuario } from "./usuario.js";

export class GerenteRelaciones extends Usuario {
  constructor(idUsuario, nombre, correo, area, activo = true) {
    super(idUsuario, nombre, correo, activo);
    this.area = area;
    /** @type {import('./queja.js').Queja[]} */
    this.quejasRecibidas = [];
  }

  getArea() {
    return this.area;
  }

  getQuejasRecibidas() {
    return [...this.quejasRecibidas];
  }

  recibirQueja(queja) {
    queja.remitir();
    this.quejasRecibidas.push(queja);
  }

  gestionarQueja(queja) {
    if (!this.quejasRecibidas.includes(queja)) {
      throw new Error("La queja no ha sido recibida por este gerente.");
    }
    queja.setEstado("en_gestion");
  }

  cerrarQueja(queja) {
    if (!this.quejasRecibidas.includes(queja)) {
      throw new Error("La queja no ha sido recibida por este gerente.");
    }
    queja.cerrar();
  }
}
