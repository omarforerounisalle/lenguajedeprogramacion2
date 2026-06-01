package televentas;

public interface NotificadorEmail {

    void enviar(String destinatario, String asunto, String contenido);
}
