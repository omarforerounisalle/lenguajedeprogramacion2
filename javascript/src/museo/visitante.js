export class Visitante {
  /**
   * @param {string} tipoConsulta
   * @param {import('./monitorVestibulo.js').MonitorVestibulo} monitor
   */
  constructor(tipoConsulta, monitor) {
    this.tipoConsulta = tipoConsulta;
    this.monitor = monitor;
  }

  getTipoConsulta() {
    return this.tipoConsulta;
  }

  getMonitor() {
    return this.monitor;
  }

  consultarObrasPorSala(idSala) {
    return this.monitor.mostrarListadoPorSala(idSala);
  }
}
