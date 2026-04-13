package museo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitorVestibulo {

    private final int idMonitor;
    private final String ubicacion;
    private final Map<Integer, Sala> salas = new HashMap<>();

    public MonitorVestibulo(int idMonitor, String ubicacion, List<Sala> listaSalas) {
        this.idMonitor = idMonitor;
        this.ubicacion = ubicacion;
        for (Sala s : listaSalas) {
            salas.put(s.getIdSala(), s);
        }
    }

    public int getIdMonitor() {
        return idMonitor;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public List<ObraArte> mostrarListadoPorSala(int idSala) {
        Sala sala = salas.get(idSala);
        if (sala == null) {
            return Collections.emptyList();
        }
        return sala.listarObras();
    }
}
