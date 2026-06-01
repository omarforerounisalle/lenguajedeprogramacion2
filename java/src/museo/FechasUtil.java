package museo;

import java.time.LocalDate;

public final class FechasUtil {

    /**
     * Suma años respetando calendario ISO (p. ej. 29 feb -> 28 feb al pasar a no bisiesto).
     */
    public static LocalDate agregarAnios(LocalDate fecha, int anios) {
        return fecha.plusYears(anios);
    }

    private FechasUtil() {
    }
}
