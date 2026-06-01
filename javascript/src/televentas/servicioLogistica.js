import { Envio } from "./envio.js";

export class ServicioLogistica {
  /**
   * @param {import('./empresaTransporte.js').EmpresaTransporte[] | undefined} empresasIniciales
   */
  constructor(empresasIniciales) {
    /** @type {import('./empresaTransporte.js').EmpresaTransporte[]} */
    this.empresas = empresasIniciales != null ? [...empresasIniciales] : [];
  }

  getEmpresas() {
    return [...this.empresas];
  }

  /** @param {import('./pedido.js').Pedido} pedido */
  seleccionarEmpresa(_pedido) {
    if (this.empresas.length === 0) {
      throw new Error("No hay empresas de transporte disponibles.");
    }
    return this.empresas[0];
  }

  /** @param {import('./pedido.js').Pedido} pedido */
  generarEnvio(pedido) {
    const envio = new Envio(
      pedido.getIdPedido(),
      pedido.getOrden().getCliente().getDireccionEnvio(),
      null,
      "pendiente"
    );
    const empresa = this.seleccionarEmpresa(pedido);
    empresa.asignarEnvio(envio);
    return envio;
  }
}
