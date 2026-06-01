import * as Estados from "./estados.js";
import { agregarAnios, anosEntre, esAntesOIgual, hoyUTC } from "./fechasUtil.js";

export class ObraArte {
  constructor(idObra, titulo, autor, periodo, valorEconomico, fechaCreacion, fechaIngreso, estado) {
    if (new.target === ObraArte) {
      throw new TypeError("No se puede instanciar la clase abstracta ObraArte");
    }
    this.idObra = idObra;
    this.titulo = titulo;
    this.autor = autor;
    this.periodo = periodo;
    this.valorEconomico = valorEconomico;
    this.fechaCreacion = fechaCreacion;
    this.fechaIngreso = fechaIngreso;
    this.estado = estado;
    this.sala = null;
    this.cesionActiva = null;
    this.colaSolicitudesCesion = [];
    /** @type {import('./restauracion.js').Restauracion[]} */
    this._restauraciones = [];
  }

  getIdObra() {
    return this.idObra;
  }

  getTitulo() {
    return this.titulo;
  }

  getAutor() {
    return this.autor;
  }

  getPeriodo() {
    return this.periodo;
  }

  getValorEconomico() {
    return this.valorEconomico;
  }

  getFechaCreacion() {
    return this.fechaCreacion;
  }

  getFechaIngreso() {
    return this.fechaIngreso;
  }

  getEstado() {
    return this.estado;
  }

  enviarARestauracion() {
    this.estado = Estados.RESTAURACION;
  }

  marcarExpuesta() {
    this.estado = Estados.EXPUESTA;
  }

  estaDisponibleParaCesion() {
    if (this.estado === Estados.RESTAURACION) return false;
    if (this.estado === Estados.CEDIDA) return false;
    if (this.estado === Estados.DANADA) return false;
    return this.estado === Estados.EXPUESTA;
  }

  calcularAntiguedad() {
    return anosEntre(this.fechaCreacion, hoyUTC());
  }

  registrarRestauracion(r) {
    this._restauraciones.push(r);
  }

  restauracionesOrdenadasPorAntiguedad() {
    return [...this._restauraciones].sort(
      (a, b) => a.getFechaInicio().getTime() - b.getFechaInicio().getTime()
    );
  }

  fechaReferenciaProximoCicloRestauracion() {
    const finales = this._restauraciones
      .map((r) => r.getFechaFin())
      .filter((f) => f != null);
    if (finales.length === 0) {
      return this.fechaIngreso;
    }
    return new Date(Math.max(...finales.map((d) => d.getTime())));
  }

  necesitaRestauracionCiclo(diaConsulta) {
    if (this.estado === Estados.RESTAURACION || this.estado === Estados.CEDIDA) {
      return false;
    }
    const ref = this.fechaReferenciaProximoCicloRestauracion();
    const limite = agregarAnios(ref, Estados.ANIOS_CICLO_RESTAURACION);
    return esAntesOIgual(limite, diaConsulta);
  }

  mostrarDetalle() {
    throw new Error("mostrarDetalle debe implementarse en la subclase");
  }
}
