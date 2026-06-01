import { Usuario } from "./usuario.js";

export class EncargadoCatalogo extends Usuario {
  constructor(idUsuario, nombre, correo, activo, contrasena, codigoEmpleado, catalogo) {
    super(idUsuario, nombre, correo, activo, contrasena);
    this.codigoEmpleado = codigoEmpleado;
    this.catalogo = catalogo;
  }

  getCodigoEmpleado() {
    return this.codigoEmpleado;
  }

  getCatalogo() {
    return this.catalogo;
  }

  registrarObra(obra) {
    if (!this.autenticar()) {
      throw new Error("Debe autenticarse antes de registrar obras.");
    }
    this.catalogo.agregarObra(obra);
  }

  actualizarObra(obra) {
    if (!this.autenticar()) {
      throw new Error("Debe autenticarse antes de actualizar obras.");
    }
    if (!this.catalogo.contieneObra(obra)) {
      this.catalogo.agregarObra(obra);
    }
    this.catalogo.tocar();
  }

  asignarSala(obra, sala) {
    if (!this.autenticar()) {
      throw new Error("Debe autenticarse antes de asignar salas.");
    }
    sala.agregarObra(obra);
  }
}
