import { ObraArte } from "./obraArte.js";

export class Escultura extends ObraArte {
  constructor(
    idObra,
    titulo,
    autor,
    periodo,
    valorEconomico,
    fechaCreacion,
    fechaIngreso,
    estado,
    material,
    estilo
  ) {
    super(idObra, titulo, autor, periodo, valorEconomico, fechaCreacion, fechaIngreso, estado);
    this.material = material;
    this.estilo = estilo;
  }

  mostrarDetalle() {
    return `Escultura: ${this.getTitulo()} (${this.getAutor()}, ${this.getPeriodo()}) - material: ${this.material}, estilo: ${this.estilo}`;
  }
}
