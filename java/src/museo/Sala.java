package museo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sala {

    private final int idSala;
    private final String nombre;
    private final String ubicacion;
    private final ArrayList<ObraArte> obrasEnSala = new ArrayList<>();

    public Sala(int idSala, String nombre, String ubicacion) {
        this.idSala = idSala;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public int getIdSala() {
        return idSala;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void agregarObra(ObraArte obra) {
        if (obrasEnSala.contains(obra)) {
            return;
        }
        if (obra.sala != null && obra.sala != this) {
            obra.sala.retirarObra(obra);
        }
        obra.sala = this;
        obrasEnSala.add(obra);
    }

    public void retirarObra(ObraArte obra) {
        obrasEnSala.remove(obra);
        if (obra.sala == this) {
            obra.sala = null;
        }
    }

    public List<ObraArte> listarObras() {
        return Collections.unmodifiableList(new ArrayList<>(obrasEnSala));
    }
}
