package museo;

import java.time.LocalDate;

public class Escultura extends ObraArte {

    private final String material;
    private final String estilo;

    public Escultura(
            int idObra,
            String titulo,
            String autor,
            String periodo,
            double valorEconomico,
            LocalDate fechaCreacion,
            LocalDate fechaIngreso,
            String estado,
            String material,
            String estilo
    ) {
        super(idObra, titulo, autor, periodo, valorEconomico, fechaCreacion, fechaIngreso, estado);
        this.material = material;
        this.estilo = estilo;
    }

    @Override
    public String mostrarDetalle() {
        return String.format(
                "Escultura: %s (%s, %s) - material: %s, estilo: %s",
                getTitulo(), getAutor(), getPeriodo(), material, estilo
        );
    }
}
