export class MonitorVestibulo {
  /**
   * @param {number} idMonitor
   * @param {string} ubicacion
   * @param {import('./sala.js').Sala[]} listaSalas
   */
  constructor(idMonitor, ubicacion, listaSalas) {
    this.idMonitor = idMonitor;
    this.ubicacion = ubicacion;
    /** @type {Map<number, import('./sala.js').Sala>} */
    this.salas = new Map();
    for (const s of listaSalas) {
      this.salas.set(s.getIdSala(), s);
    }
  }

  getIdMonitor() {
    return this.idMonitor;
  }

  getUbicacion() {
    return this.ubicacion;
  }

  mostrarListadoPorSala(idSala) {
    const sala = this.salas.get(idSala);
    if (sala == null) {
      return [];
    }
    return sala.listarObras();
  }
}
