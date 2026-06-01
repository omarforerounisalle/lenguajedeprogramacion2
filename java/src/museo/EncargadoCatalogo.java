package museo;

public class EncargadoCatalogo extends Usuario {

    private final String codigoEmpleado;
    private final CatalogoObras catalogo;

    public EncargadoCatalogo(
            int idUsuario,
            String nombre,
            String correo,
            boolean activo,
            String contrasena,
            String codigoEmpleado,
            CatalogoObras catalogo
    ) {
        super(idUsuario, nombre, correo, activo, contrasena);
        this.codigoEmpleado = codigoEmpleado;
        this.catalogo = catalogo;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public CatalogoObras getCatalogo() {
        return catalogo;
    }

    public void registrarObra(ObraArte obra) {
        if (!autenticar()) {
            throw new IllegalStateException("Debe autenticarse antes de registrar obras.");
        }
        catalogo.agregarObra(obra);
    }

    public void actualizarObra(ObraArte obra) {
        if (!autenticar()) {
            throw new IllegalStateException("Debe autenticarse antes de actualizar obras.");
        }
        if (!catalogo.contieneObra(obra)) {
            catalogo.agregarObra(obra);
        }
        catalogo.tocar();
    }

    public void asignarSala(ObraArte obra, Sala sala) {
        if (!autenticar()) {
            throw new IllegalStateException("Debe autenticarse antes de asignar salas.");
        }
        sala.agregarObra(obra);
    }
}
