export class SuscripcionCatalogo {
  constructor(idSuscripcion, correoDestino, frecuencia, activa) {
    this.idSuscripcion = idSuscripcion;
    this.correoDestino = correoDestino;
    this.frecuencia = frecuencia;
    this.activa = activa;
  }

  getIdSuscripcion() {
    return this.idSuscripcion;
  }

  getCorreoDestino() {
    return this.correoDestino;
  }

  getFrecuencia() {
    return this.frecuencia;
  }

  isActiva() {
    return this.activa;
  }

  activar() {
    this.activa = true;
  }

  desactivar() {
    this.activa = false;
  }

  enviarCatalogo(catalogo, notificador) {
    if (!this.activa) {
      throw new Error("La suscripcion esta inactiva.");
    }
    const nombres = catalogo.listarProductos().map((p) => p.getNombre()).join(", ");
    const asunto = "Catalogo TeleVentas";
    const contenido = `Productos disponibles: ${nombres}`;
    notificador.enviar(this.correoDestino, asunto, contenido);
  }
}
