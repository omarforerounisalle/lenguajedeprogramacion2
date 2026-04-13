package museo;

public class MuseoColaborador {

    private final int idMuseo;
    private final String nombre;
    private final String ciudad;
    private final String pais;

    public MuseoColaborador(int idMuseo, String nombre, String ciudad, String pais) {
        this.idMuseo = idMuseo;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public int getIdMuseo() {
        return idMuseo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void recibirCesion() {
        // Hook UML; logica principal en Director/Cesion
    }

    public void solicitarObra() {
        // Hook UML
    }
}
