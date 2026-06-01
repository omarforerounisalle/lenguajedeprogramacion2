package museo;

import java.util.List;

public class Visitante {

    private final String tipoConsulta;
    private final MonitorVestibulo monitor;

    public Visitante(String tipoConsulta, MonitorVestibulo monitor) {
        this.tipoConsulta = tipoConsulta;
        this.monitor = monitor;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public MonitorVestibulo getMonitor() {
        return monitor;
    }

    public List<ObraArte> consultarObrasPorSala(int idSala) {
        return monitor.mostrarListadoPorSala(idSala);
    }
}
