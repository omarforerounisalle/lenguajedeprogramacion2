package museo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class CatalogoObras {

    private final ArrayList<ObraArte> obras = new ArrayList<>();
    private LocalDate fechaActualizacion = LocalDate.now();

    void tocar() {
        this.fechaActualizacion = LocalDate.now();
    }

    public LocalDate getFechaActualizacion() {
        return fechaActualizacion;
    }

    public List<ObraArte> listarObras() {
        return Collections.unmodifiableList(new ArrayList<>(obras));
    }

    public ObraArte buscarObra(int idObra) {
        for (ObraArte o : obras) {
            if (o.getIdObra() == idObra) {
                return o;
            }
        }
        throw new NoSuchElementException("No existe obra con id " + idObra);
    }

    public void agregarObra(ObraArte obra) {
        obras.add(obra);
        tocar();
    }

    public void eliminarObra(int idObra) {
        obras.removeIf(o -> o.getIdObra() == idObra);
        tocar();
    }

    public boolean contieneObra(ObraArte obra) {
        return obras.contains(obra);
    }
}
