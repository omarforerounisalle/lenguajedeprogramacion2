export class Usuario {
  constructor(idUsuario, nombre, correo, activo, contrasena) {
    if (new.target === Usuario) {
      throw new TypeError("No se puede instanciar la clase abstracta Usuario");
    }
    this.idUsuario = idUsuario;
    this.nombre = nombre;
    this.correo = correo;
    this.activo = activo;
    this.contrasena = contrasena;
    this._sesionActiva = false;
  }

  iniciarSesion(contrasena) {
    if (!this.activo) {
      this._sesionActiva = false;
      return false;
    }
    const ok = this.contrasena === contrasena;
    this._sesionActiva = ok;
    return ok;
  }

  cerrarSesion() {
    this._sesionActiva = false;
  }

  autenticar() {
    return this.activo && this._sesionActiva;
  }

  obtenerDatos() {
    return `Usuario(id=${this.idUsuario}, nombre=${this.nombre}, correo=${this.correo}, activo=${this.activo})`;
  }
}
