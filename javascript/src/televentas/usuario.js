export class Usuario {
  constructor(idUsuario, nombre, correo, activo) {
    if (new.target === Usuario) {
      throw new TypeError("No se puede instanciar la clase abstracta Usuario");
    }
    this.idUsuario = idUsuario;
    this.nombre = nombre;
    this.correo = correo;
    this.activo = activo;
  }

  autenticar() {
    return this.activo;
  }

  obtenerDatos() {
    return `Usuario(id=${this.idUsuario}, nombre=${this.nombre}, correo=${this.correo})`;
  }

  getIdUsuario() {
    return this.idUsuario;
  }

  getNombre() {
    return this.nombre;
  }

  getCorreo() {
    return this.correo;
  }

  isActivo() {
    return this.activo;
  }
}
