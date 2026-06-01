package televentas;

import java.util.stream.Collectors;

public class SuscripcionCatalogo {

    private final int idSuscripcion;
    private final String correoDestino;
    private final String frecuencia;
    private boolean activa;

    public SuscripcionCatalogo(int idSuscripcion, String correoDestino, String frecuencia, boolean activa) {
        this.idSuscripcion = idSuscripcion;
        this.correoDestino = correoDestino;
        this.frecuencia = frecuencia;
        this.activa = activa;
    }

    public int getIdSuscripcion() {
        return idSuscripcion;
    }

    public String getCorreoDestino() {
        return correoDestino;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public boolean isActiva() {
        return activa;
    }

    public void activar() {
        this.activa = true;
    }

    public void desactivar() {
        this.activa = false;
    }

    public void enviarCatalogo(Catalogo catalogo, NotificadorEmail notificador) {
        if (!activa) {
            throw new IllegalStateException("La suscripcion esta inactiva.");
        }
        String nombres = catalogo.listarProductos().stream()
                .map(Producto::getNombre)
                .collect(Collectors.joining(", "));
        String asunto = "Catalogo TeleVentas";
        String contenido = "Productos disponibles: " + nombres;
        notificador.enviar(correoDestino, asunto, contenido);
    }
}
