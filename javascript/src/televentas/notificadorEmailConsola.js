import { NotificadorEmail } from "./notificadorEmail.js";

export class NotificadorEmailConsola extends NotificadorEmail {
  constructor() {
    super();
  }

  enviar(destinatario, asunto, contenido) {
    console.log();
    console.log("--- CORREO ENVIADO ---");
    console.log(`Para: ${destinatario}`);
    console.log(`Asunto: ${asunto}`);
    console.log(`Contenido: ${contenido}`);
    console.log("----------------------");
    console.log();
  }
}
