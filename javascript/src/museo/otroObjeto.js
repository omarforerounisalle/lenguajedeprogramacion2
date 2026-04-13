import { ObraArte } from "./obraArte.js";

export class OtroObjeto extends ObraArte {
  constructor(
    idObra,
    titulo,
    autor,
    periodo,
    valorEconomico,
    fechaCreacion,
    fechaIngreso,
    estado,
    tipoObjeto
  ) {
    super(idObra, titulo, autor, periodo, valorEconomico, fechaCreacion, fechaIngreso, estado);
    this.tipoObjeto = tipoObjeto;
  }

  mostrarDetalle() {
    return `Otro objeto (${this.tipoObjeto}): ${this.getTitulo()} (${this.getAutor()}, ${this.getPeriodo()})`;
  }
}
