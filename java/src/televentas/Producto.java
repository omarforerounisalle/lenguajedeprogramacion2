package televentas;

public class Producto {

    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private double precio;
    private int cantidadDisponible;

    public Producto(String codigo, String nombre, String descripcion, double precio, int cantidadDisponible) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public boolean estaDisponible(int cantidad) {
        return cantidadDisponible >= cantidad;
    }

    public void actualizarPrecio(double nuevoPrecio) {
        if (nuevoPrecio <= 0) {
            throw new IllegalArgumentException("El nuevo precio debe ser mayor que cero.");
        }
        this.precio = nuevoPrecio;
    }

    public void actualizarStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad disponible no puede ser negativa.");
        }
        this.cantidadDisponible = cantidad;
    }
}
