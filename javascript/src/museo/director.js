import * as Estados from "./estados.js";
import { Usuario } from "./usuario.js";
import { Cesion } from "./cesion.js";
import { diasEntre, hoyUTC, sumarDias } from "./fechasUtil.js";

export class Director extends Usuario {
  constructor(idUsuario, nombre, correo, activo, contrasena, cargo, catalogo) {
    super(idUsuario, nombre, correo, activo, contrasena);
    this.cargo = cargo;
    this.catalogo = catalogo;
    /** @type {Cesion[]} */
    this.cesiones = [];
    /** @type {import('./museoColaborador.js').MuseoColaborador[]} */
    this.museosColaboradores = [];
  }

  getCargo() {
    return this.cargo;
  }

  getCesiones() {
    return [...this.cesiones];
  }

  getMuseosColaboradores() {
    return [...this.museosColaboradores];
  }

  registrarMuseoColaborador(museo) {
    if (!this.autenticar()) {
      throw new Error("Debe autenticarse.");
    }
    if (!this.museosColaboradores.includes(museo)) {
      this.museosColaboradores.push(museo);
    }
  }

  /**
   * @returns {Cesion | null}
   */
  cederObra(obra, museo, fechaInicio, fechaFin, importePagado) {
    if (!this.autenticar()) {
      throw new Error("Debe autenticarse.");
    }
    if (obra.estado === Estados.CEDIDA) {
      obra.colaSolicitudesCesion.push(museo);
      museo.solicitarObra();
      return null;
    }
    if (!obra.estaDisponibleParaCesion()) {
      throw new Error("La obra no esta disponible para cesion.");
    }
    const cesion = new Cesion(obra, museo, fechaInicio, fechaFin, importePagado, "pendiente");
    cesion.iniciar();
    this.cesiones.push(cesion);
    museo.recibirCesion();
    return cesion;
  }

  /**
   * @returns {Cesion | null}
   */
  finalizarCesionYAsignarSiguiente(cesion) {
    if (!this.autenticar()) {
      throw new Error("Debe autenticarse.");
    }
    const obra = cesion.getObra();
    const duracion = diasEntre(cesion.getFechaInicio(), cesion.getFechaFin());
    cesion.finalizar();
    if (obra.colaSolicitudesCesion.length > 0) {
      const siguiente = obra.colaSolicitudesCesion.shift();
      const inicio = hoyUTC();
      const nuevaFin = sumarDias(inicio, duracion);
      return this.cederObra(obra, siguiente, inicio, nuevaFin, cesion.getImportePagado());
    }
    return null;
  }

  consultarValorTotal() {
    if (!this.autenticar()) {
      throw new Error("Debe autenticarse.");
    }
    let suma = 0;
    for (const o of this.catalogo.listarObras()) {
      suma += o.getValorEconomico();
    }
    return suma;
  }
}
