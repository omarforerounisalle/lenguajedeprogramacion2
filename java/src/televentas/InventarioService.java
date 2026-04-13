package televentas;

import java.util.Optional;

public interface InventarioService {

    Optional<Producto> obtenerProducto(String codigo);

    int consultarDisponibilidad(String codigo);

    void actualizarStock(String codigo, int cantidad);
}
