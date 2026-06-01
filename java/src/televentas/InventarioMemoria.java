package televentas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InventarioMemoria implements InventarioService {

    private final Map<String, Producto> productos = new HashMap<>();

    public InventarioMemoria(List<Producto> productosIniciales) {
        for (Producto producto : productosIniciales) {
            this.productos.put(producto.getCodigo(), producto);
        }
    }

    @Override
    public Optional<Producto> obtenerProducto(String codigo) {
        return Optional.ofNullable(productos.get(codigo));
    }

    @Override
    public int consultarDisponibilidad(String codigo) {
        Optional<Producto> p = obtenerProducto(codigo);
        if (!p.isPresent()) {
            return 0;
        }
        return p.get().getCantidadDisponible();
    }

    @Override
    public void actualizarStock(String codigo, int cantidad) {
        Producto producto = productos.get(codigo);
        if (producto == null) {
            throw new IllegalArgumentException("Producto con codigo " + codigo + " no encontrado.");
        }
        producto.actualizarStock(cantidad);
    }
}
