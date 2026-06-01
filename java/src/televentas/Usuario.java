package televentas;

public abstract class Usuario {

    private final int idUsuario;
    private final String nombre;
    private final String correo;
    private final boolean activo;

    protected Usuario(int idUsuario, String nombre, String correo, boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.activo = activo;
    }

    public boolean autenticar() {
        return activo;
    }

    public String obtenerDatos() {
        return String.format(
                "Usuario(id=%d, nombre=%s, correo=%s)",
                idUsuario, nombre, correo
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
