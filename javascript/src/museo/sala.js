export class Sala {
  constructor(idSala, nombre, ubicacion) {
    this.idSala = idSala;
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    /** @type {import('./obraArte.js').ObraArte[]} */
    this._obrasEnSala = [];
  }

  getIdSala() {
    return this.idSala;
  }

  getNombre() {
    return this.nombre;
  }

  getUbicacion() {
    return this.ubicacion;
  }

  agregarObra(obra) {
    if (this._obrasEnSala.includes(obra)) {
      return;
    }
    if (obra.sala != null && obra.sala !== this) {
      obra.sala.retirarObra(obra);
    }
    obra.sala = this;
    this._obrasEnSala.push(obra);
  }

  retirarObra(obra) {
    this._obrasEnSala = this._obrasEnSala.filter((o) => o !== obra);
    if (obra.sala === this) {
      obra.sala = null;
    }
  }

  listarObras() {
    return [...this._obrasEnSala];
  }
}
