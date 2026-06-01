package museo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProcesoRestauracionAutomatica {

    private final CatalogoObras catalogo;

    public ProcesoRestauracionAutomatica(CatalogoObras catalogo) {
        this.catalogo = catalogo;
    }

    public List<ObraArte> identificarObrasParaRestauracion() {
        return identificarObrasParaRestauracion(LocalDate.now());
    }

    public List<ObraArte> identificarObrasParaRestauracion(LocalDate dia) {
        ArrayList<ObraArte> out = new ArrayList<>();
        for (ObraArte o : catalogo.listarObras()) {
            if (o.necesitaRestauracionCiclo(dia)) {
                out.add(o);
            }
        }
        return out;
    }
}
