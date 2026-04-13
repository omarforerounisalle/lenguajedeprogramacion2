package museo;

public abstract class Usuario {

    private final int idUsuario;
    private final String nombre;
    private final String correo;
    private final boolean activo;
    private final String contrasena;
    private boolean sesionActiva;

    protected Usuario(
            int idUsuario,
            String nombre,
            String correo,
            boolean activo,
            String contrasena
    ) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.activo = activo;
        this.contrasena = contrasena;
        this.sesionActiva = false;
    }

    public boolean iniciarSesion(String contrasena) {
        if (!activo) {
            sesionActiva = false;
            return false;
        }
        boolean ok = this.contrasena.equals(contrasena);
        sesionActiva = ok;
        return ok;
    }

    public void cerrarSesion() {
        sesionActiva = false;
    }

    public boolean autenticar() {
        return activo && sesionActiva;
    }

    public String obtenerDatos() {
        return String.format(
                "Usuario(id=%d, nombre=%s, correo=%s, activo=%s)",
                idUsuario, nombre, correo, activo
        );
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public boolean isActivo() {
        return activo;
    }
}
