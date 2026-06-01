package televentas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenteRelaciones extends Usuario {

    private final String area;
    private final ArrayList<Queja> quejasRecibidas = new ArrayList<>();

    public GerenteRelaciones(int idUsuario, String nombre, String correo, String area) {
        super(idUsuario, nombre, correo, true);
        this.area = area;
    }

    public GerenteRelaciones(int idUsuario, String nombre, String correo, String area, boolean activo) {
        super(idUsuario, nombre, correo, activo);
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public List<Queja> getQuejasRecibidas() {
        return Collections.unmodifiableList(new ArrayList<>(quejasRecibidas));
    }

    public void recibirQueja(Queja queja) {
        queja.remitir();
        quejasRecibidas.add(queja);
    }

    public void gestionarQueja(Queja queja) {
        if (!quejasRecibidas.contains(queja)) {
            throw new IllegalArgumentException("La queja no ha sido recibida por este gerente.");
        }
        queja.setEstado("en_gestion");
    }

    public void cerrarQueja(Queja queja) {
        if (!quejasRecibidas.contains(queja)) {
            throw new IllegalArgumentException("La queja no ha sido recibida por este gerente.");
        }
        queja.cerrar();
    }
}
