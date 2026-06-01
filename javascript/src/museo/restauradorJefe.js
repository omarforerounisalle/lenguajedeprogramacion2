import { Usuario } from "./usuario.js";
import { Restauracion } from "./restauracion.js";

export class RestauradorJefe extends Usuario {
  constructor(idUsuario, nombre, correo, activo, contrasena, especialidad) {
    super(idUsuario, nombre, correo, activo, contrasena);
    this.especialidad = especialidad;
    /** @type {Restauracion[]} */
    this.restauracionesGestionadas = [];
  }

  getEspecialidad() {
    return this.especialidad;
  }

  iniciarRestauracion(obra, tipoRestauracion, fechaInicio, motivo) {
    if (!this.autenticar()) {
      throw new Error("Debe autenticarse.");
    }
    const r = new Restauracion(obra, tipoRestauracion, fechaInicio, null, motivo);
    r.iniciar();
    obra.registrarRestauracion(r);
    this.restauracionesGestionadas.push(r);
    return r;
  }

  finalizarRestauracion(restauracion) {
    if (!this.autenticar()) {
      throw new Error("Debe autenticarse.");
    }
    restauracion.finalizar();
  }

  consultarRestauraciones() {
    if (!this.autenticar()) {
      throw new Error("Debe autenticarse.");
    }
    return [...this.restauracionesGestionadas].sort(
      (a, b) => a.getFechaInicio().getTime() - b.getFechaInicio().getTime()
    );
  }

  consultarRestauracionesPorObra(obra) {
    if (!this.autenticar()) {
      throw new Error("Debe autenticarse.");
    }
    return obra.restauracionesOrdenadasPorAntiguedad();
  }
}
