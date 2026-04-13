package televentas;

import java.time.LocalDate;

public abstract class Pago {

    protected double monto;
    protected LocalDate fechaPago;
    protected String estado;

    protected Pago(double monto, LocalDate fechaPago, String estado) {
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.estado = estado;
    }

    protected Pago(double monto, LocalDate fechaPago) {
        this(monto, fechaPago, "pendiente");
    }

    public abstract boolean procesar();

    public abstract boolean validar();

    public double getMonto() {
        return monto;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public String getEstado() {
        return estado;
    }
}
