package televentas;

import java.time.LocalDate;

public class PagoTarjetaCredito extends Pago {

    private final String numeroEnmascarado;
    private final String titular;
    private final String franquicia;
    private final String fechaVencimiento;

    public PagoTarjetaCredito(
            double monto,
            LocalDate fechaPago,
            String numeroEnmascarado,
            String titular,
            String franquicia,
            String fechaVencimiento
    ) {
        super(monto, fechaPago);
        this.numeroEnmascarado = numeroEnmascarado;
        this.titular = titular;
        this.franquicia = franquicia;
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public boolean validar() {
        return monto > 0
                && numeroEnmascarado != null && numeroEnmascarado.length() >= 4
                && titular != null && !titular.trim().isEmpty()
                && franquicia != null && !franquicia.trim().isEmpty()
                && fechaVencimiento != null && !fechaVencimiento.trim().isEmpty();
    }

    @Override
    public boolean procesar() {
        if (!validar()) {
            estado = "rechazado";
            return false;
        }
        estado = "aprobado";
        return true;
    }
}
