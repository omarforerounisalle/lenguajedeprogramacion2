package televentas;

public class NotificadorEmailConsola implements NotificadorEmail {

    @Override
    public void enviar(String destinatario, String asunto, String contenido) {
        System.out.println();
        System.out.println("--- CORREO ENVIADO ---");
        System.out.println("Para: " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("Contenido: " + contenido);
        System.out.println("----------------------");
        System.out.println();
    }
}
