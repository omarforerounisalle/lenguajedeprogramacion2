package televentas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Catalogo {

    private final ArrayList<Producto> productos = new ArrayList<>();
    private LocalDate fechaActualizacion = LocalDate.now();

    public Catalogo() {
    }

    public Catalogo(List<Producto> inicial) {
        productos.addAll(inicial);
        fechaActualizacion = LocalDate.now();
    }

    public LocalDate getFechaActualizacion() {
        return fechaActualizacion;
    }

    public List<Producto> listarProductos() {
        return Collections.unmodifiableList(new ArrayList<>(productos));
    }

    public Optional<Producto> buscarProducto(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equals(codigo)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public void agregarProducto(Producto producto) {
        if (buscarProducto(producto.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un producto con codigo " + producto.getCodigo() + ".");
        }
        productos.add(producto);
        fechaActualizacion = LocalDate.now();
    }

    public void eliminarProducto(String codigo) {
        Optional<Producto> p = buscarProducto(codigo);
        if (!p.isPresent()) {
            throw new IllegalArgumentException("No existe un producto con codigo " + codigo + ".");
        }
        productos.remove(p.get());
        fechaActualizacion = LocalDate.now();
    }
}
