import { hoyUTC } from "./fechasUtil.js";

let _idSeq = 0;

export class Restauracion {
  /**
   * @param {import('./obraArte.js').ObraArte} obra
   * @param {string} tipoRestauracion
   * @param {Date} fechaInicio
   * @param {Date | null} fechaFin
   * @param {string} motivo
   */
  constructor(obra, tipoRestauracion, fechaInicio, fechaFin, motivo) {
    this.idRestauracion = ++_idSeq;
    this.obra = obra;
    this.tipoRestauracion = tipoRestauracion;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.motivo = motivo;
  }

  getIdRestauracion() {
    return this.idRestauracion;
  }

  getObra() {
    return this.obra;
  }

  getTipoRestauracion() {
    return this.tipoRestauracion;
  }

  getFechaInicio() {
    return this.fechaInicio;
  }

  /** @returns {Date | null} */
  getFechaFin() {
    return this.fechaFin;
  }

  getMotivo() {
    return this.motivo;
  }

  iniciar() {
    this.obra.enviarARestauracion();
  }

  finalizar(fechaFin = hoyUTC()) {
    this.fechaFin = fechaFin;
    this.obra.marcarExpuesta();
  }

  estaActiva() {
    return this.fechaFin == null;
  }
}
