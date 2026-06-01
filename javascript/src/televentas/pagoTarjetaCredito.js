import { Pago } from "./pago.js";

export class PagoTarjetaCredito extends Pago {
  constructor(monto, fechaPago, numeroEnmascarado, titular, franquicia, fechaVencimiento) {
    super(monto, fechaPago, "pendiente");
    this.numeroEnmascarado = numeroEnmascarado;
    this.titular = titular;
    this.franquicia = franquicia;
    this.fechaVencimiento = fechaVencimiento;
  }

  validar() {
    return (
      this.monto > 0 &&
      this.numeroEnmascarado != null &&
      this.numeroEnmascarado.length >= 4 &&
      this.titular != null &&
      this.titular.trim() !== "" &&
      this.franquicia != null &&
      this.franquicia.trim() !== "" &&
      this.fechaVencimiento != null &&
      this.fechaVencimiento.trim() !== ""
    );
  }

  procesar() {
    if (!this.validar()) {
      this.estado = "rechazado";
      return false;
    }
    this.estado = "aprobado";
    return true;
  }
}
