import { hoyUTC } from "./fechasUtil.js";

export class Envio {
  /**
   * @param {number} idEnvio
   * @param {string} direccionDestino
   * @param {Date | null} fechaSalida
   * @param {string} estado
   */
  constructor(idEnvio, direccionDestino, fechaSalida, estado) {
    this.idEnvio = idEnvio;
    this.direccionDestino = direccionDestino;
    this.fechaSalida = fechaSalida;
    this.estado = estado;
  }

  getIdEnvio() {
    return this.idEnvio;
  }

  getDireccionDestino() {
    return this.direccionDestino;
  }

  /** @returns {Date | null} */
  getFechaSalida() {
    return this.fechaSalida;
  }

  getEstado() {
    return this.estado;
  }

  programar() {
    this.estado = "programado";
  }

  despachar() {
    this.estado = "despachado";
    this.fechaSalida = hoyUTC();
  }

  entregar() {
    this.estado = "entregado";
  }
}
