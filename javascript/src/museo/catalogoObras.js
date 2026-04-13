import { hoyUTC } from "./fechasUtil.js";

export class CatalogoObras {
  constructor() {
    /** @type {import('./obraArte.js').ObraArte[]} */
    this._obras = [];
    this.fechaActualizacion = hoyUTC();
  }

  tocar() {
    this.fechaActualizacion = hoyUTC();
  }

  getFechaActualizacion() {
    return this.fechaActualizacion;
  }

  listarObras() {
    return [...this._obras];
  }

  buscarObra(idObra) {
    for (const o of this._obras) {
      if (o.getIdObra() === idObra) {
        return o;
      }
    }
    throw new Error(`No existe obra con id ${idObra}`);
  }

  agregarObra(obra) {
    this._obras.push(obra);
    this.tocar();
  }

  eliminarObra(idObra) {
    this._obras = this._obras.filter((o) => o.getIdObra() !== idObra);
    this.tocar();
  }

  contieneObra(obra) {
    return this._obras.includes(obra);
  }
}
