package museo;

import java.time.LocalDate;

public class Cuadro extends ObraArte {

    private final String tecnica;
    private final String estilo;

    public Cuadro(
            int idObra,
            String titulo,
            String autor,
            String periodo,
            double valorEconomico,
            LocalDate fechaCreacion,
            LocalDate fechaIngreso,
            String estado,
            String tecnica,
            String estilo
    ) {
        super(idObra, titulo, autor, periodo, valorEconomico, fechaCreacion, fechaIngreso, estado);
        this.tecnica = tecnica;
        this.estilo = estilo;
    }

    @Override
    public String mostrarDetalle() {
        return String.format(
                "Cuadro: %s (%s, %s) - tecnica: %s, estilo: %s",
                getTitulo(), getAutor(), getPeriodo(), tecnica, estilo
        );
    }
}
