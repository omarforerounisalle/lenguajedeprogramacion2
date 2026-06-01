package televentas;

public class DetalleOrden {

    private final int cantidad;
    private final double precioUnitario;
    private final Producto producto;

    public DetalleOrden(int cantidad, double precioUnitario, Producto producto) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public Producto getProducto() {
        return producto;
    }

    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }
}
