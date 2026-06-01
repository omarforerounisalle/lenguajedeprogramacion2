import * as Estados from "./estados.js";

let _idSeq = 0;

export class Cesion {
  /**
   * @param {import('./obraArte.js').ObraArte} obra
   * @param {import('./museoColaborador.js').MuseoColaborador} museo
   * @param {Date} fechaInicio
   * @param {Date} fechaFin
   * @param {number} importePagado
   * @param {string} estado
   */
  constructor(obra, museo, fechaInicio, fechaFin, importePagado, estado) {
    this.idCesion = ++_idSeq;
    this.obra = obra;
    this.museo = museo;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.importePagado = importePagado;
    this.estado = estado;
  }

  getIdCesion() {
    return this.idCesion;
  }

  getObra() {
    return this.obra;
  }

  getMuseo() {
    return this.museo;
  }

  getFechaInicio() {
    return this.fechaInicio;
  }

  getFechaFin() {
    return this.fechaFin;
  }

  getImportePagado() {
    return this.importePagado;
  }

  getEstado() {
    return this.estado;
  }

  iniciar() {
    this.obra.estado = Estados.CEDIDA;
    this.obra.cesionActiva = this;
    this.estado = "activa";
  }

  finalizar() {
    this.estado = "finalizada";
    if (this.obra.cesionActiva === this) {
      this.obra.cesionActiva = null;
    }
    this.obra.marcarExpuesta();
  }

  estaActiva() {
    return this.estado === "activa";
  }
}
