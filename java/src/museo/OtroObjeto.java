package museo;

import java.time.LocalDate;

public class OtroObjeto extends ObraArte {

    private final String tipoObjeto;

    public OtroObjeto(
            int idObra,
            String titulo,
            String autor,
            String periodo,
            double valorEconomico,
            LocalDate fechaCreacion,
            LocalDate fechaIngreso,
            String estado,
            String tipoObjeto
    ) {
        super(idObra, titulo, autor, periodo, valorEconomico, fechaCreacion, fechaIngreso, estado);
        this.tipoObjeto = tipoObjeto;
    }

    @Override
    public String mostrarDetalle() {
        return String.format(
                "Otro objeto (%s): %s (%s, %s)",
                tipoObjeto, getTitulo(), getAutor(), getPeriodo()
        );
    }
}
