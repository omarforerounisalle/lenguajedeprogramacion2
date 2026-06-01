export class NotificadorEmail {
  constructor() {
    if (new.target === NotificadorEmail) {
      throw new TypeError("No se puede instanciar la clase abstracta NotificadorEmail");
    }
  }

  enviar(_destinatario, _asunto, _contenido) {
    throw new TypeError("Metodo abstracto");
  }
}
