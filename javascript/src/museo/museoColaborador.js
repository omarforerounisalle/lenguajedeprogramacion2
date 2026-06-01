export class MuseoColaborador {
  constructor(idMuseo, nombre, ciudad, pais) {
    this.idMuseo = idMuseo;
    this.nombre = nombre;
    this.ciudad = ciudad;
    this.pais = pais;
  }

  getIdMuseo() {
    return this.idMuseo;
  }

  getNombre() {
    return this.nombre;
  }

  getCiudad() {
    return this.ciudad;
  }

  getPais() {
    return this.pais;
  }

  recibirCesion() {
    // Hook UML; logica principal en Director/Cesion
  }

  solicitarObra() {
    // Hook UML
  }
}
