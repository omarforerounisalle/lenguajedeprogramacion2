package televentas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliente extends Usuario {

    private final String direccionEnvio;
    private final String telefono;
    private final ArrayList<OrdenCompra> ordenes = new ArrayList<>();
    private final ArrayList<Queja> quejas = new ArrayList<>();
    private final ArrayList<SuscripcionCatalogo> suscripciones = new ArrayList<>();

    public Cliente(
            int idUsuario,
            String nombre,
            String correo,
            String direccionEnvio,
            String telefono
    ) {
        super(idUsuario, nombre, correo, true);
        this.direccionEnvio = direccionEnvio;
        this.telefono = telefono;
    }

    public Cliente(
            int idUsuario,
            String nombre,
            String correo,
            String direccionEnvio,
            String telefono,
            boolean activo
    ) {
        super(idUsuario, nombre, correo, activo);
        this.direccionEnvio = direccionEnvio;
        this.telefono = telefono;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public String getTelefono() {
        return telefono;
    }

    public List<OrdenCompra> getOrdenes() {
        return Collections.unmodifiableList(new ArrayList<>(ordenes));
    }

    public List<Queja> getQuejas() {
        return Collections.unmodifiableList(new ArrayList<>(quejas));
    }

    public List<SuscripcionCatalogo> getSuscripciones() {
        return Collections.unmodifiableList(new ArrayList<>(suscripciones));
    }

    public List<Producto> consultarCatalogo(Catalogo catalogo) {
        return catalogo.listarProductos();
    }

    public SuscripcionCatalogo solicitarCatalogo(int idSuscripcion, String frecuencia) {
        SuscripcionCatalogo suscripcion = new SuscripcionCatalogo(
                idSuscripcion,
                getCorreo(),
                frecuencia,
                true
        );
        suscripciones.add(suscripcion);
        return suscripcion;
    }

    public OrdenCompra crearOrden(int idOrden) {
        OrdenCompra orden = new OrdenCompra(idOrden, LocalDate.now(), this);
        ordenes.add(orden);
        return orden;
    }

    public Queja presentarQueja(int idQueja, String motivo, String descripcion, OrdenCompra orden) {
        Queja queja = new Queja(idQueja, LocalDate.now(), motivo, descripcion, orden);
        quejas.add(queja);
        return queja;
    }

    public void cancelarOrden(OrdenCompra orden) {
        if (!ordenes.contains(orden)) {
            throw new IllegalArgumentException("La orden no pertenece a este cliente.");
        }
        orden.cancelar();
    }
}
