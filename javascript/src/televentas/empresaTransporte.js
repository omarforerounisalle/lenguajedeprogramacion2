export class EmpresaTransporte {
  constructor(idEmpresa, nombre, cobertura) {
    this.idEmpresa = idEmpresa;
    this.nombre = nombre;
    this.cobertura = cobertura;
  }

  getIdEmpresa() {
    return this.idEmpresa;
  }

  getNombre() {
    return this.nombre;
  }

  getCobertura() {
    return this.cobertura;
  }

  calcularTarifa() {
    return 15000.0;
  }

  /** @param {import('./envio.js').Envio} envio */
  asignarEnvio(envio) {
    envio.programar();
  }
}
