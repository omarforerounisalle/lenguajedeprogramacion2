import { hoyUTC } from "./fechasUtil.js";

export class ProcesoRestauracionAutomatica {
  /**
   * @param {import('./catalogoObras.js').CatalogoObras} catalogo
   */
  constructor(catalogo) {
    this.catalogo = catalogo;
  }

  identificarObrasParaRestauracion(dia = hoyUTC()) {
    const out = [];
    for (const o of this.catalogo.listarObras()) {
      if (o.necesitaRestauracionCiclo(dia)) {
        out.push(o);
      }
    }
    return out;
  }
}
