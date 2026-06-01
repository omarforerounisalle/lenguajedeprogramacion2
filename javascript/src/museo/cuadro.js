import { ObraArte } from "./obraArte.js";

export class Cuadro extends ObraArte {
  constructor(
    idObra,
    titulo,
    autor,
    periodo,
    valorEconomico,
    fechaCreacion,
    fechaIngreso,
    estado,
    tecnica,
    estilo
  ) {
    super(idObra, titulo, autor, periodo, valorEconomico, fechaCreacion, fechaIngreso, estado);
    this.tecnica = tecnica;
    this.estilo = estilo;
  }

  mostrarDetalle() {
    return `Cuadro: ${this.getTitulo()} (${this.getAutor()}, ${this.getPeriodo()}) - tecnica: ${this.tecnica}, estilo: ${this.estilo}`;
  }
}
