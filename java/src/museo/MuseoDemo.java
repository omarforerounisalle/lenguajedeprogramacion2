package museo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Demostracion en consola alineada con {@code python/museo.py} (main).
 */
public final class MuseoDemo {

    private MuseoDemo() {
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("SIMULACION: GESTION DEL MUSEO (Java)");
        System.out.println("============================================================");

        CatalogoObras catalogo = new CatalogoObras();
        Sala salaRenacimiento = new Sala(1, "Renacimiento", "Planta 1 - Ala norte");
        Sala salaModerna = new Sala(2, "Arte moderno", "Planta 2");

        EncargadoCatalogo encargado = new EncargadoCatalogo(
                1, "Ana Martinez", "ana@museo.org", true, "cat2024",
                "EC-001", catalogo
        );

        Director director = new Director(
                2, "Luis Herrera", "director@museo.org", true, "dir2024",
                "Director general", catalogo
        );

        RestauradorJefe restaurador = new RestauradorJefe(
                3, "Elena Vargas", "restauracion@museo.org", true, "res2024",
                "Pintura sobre lienzo"
        );

        System.out.println();
        System.out.println("=== INTENTO SIN AUTENTICACION (encargado del catalogo) ===");
        Cuadro cuadro = new Cuadro(
                1, "Noche estrellada", "Vincent van Gogh", "s. XIX",
                850_000_000.0,
                LocalDate.of(1889, 6, 1),
                LocalDate.of(2010, 3, 15),
                EstadosMuseo.EXPUESTA,
                "oleo sobre lienzo", "postimpresionismo"
        );
        try {
            encargado.registrarObra(cuadro);
        } catch (IllegalStateException e) {
            System.out.println("Rechazado (correcto): " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== ENCARGADO DEL CATALOGO: sesion y registro de obras ===");
        encargado.iniciarSesion("cat2024");
        System.out.println("Sesion iniciada: " + encargado.obtenerDatos());

        Escultura escultura = new Escultura(
                2, "El pensador", "Auguste Rodin", "s. XIX",
                420_000_000.0,
                LocalDate.of(1902, 1, 1),
                LocalDate.of(2005, 6, 1),
                EstadosMuseo.EXPUESTA,
                "bronce", "modernista"
        );
        OtroObjeto otro = new OtroObjeto(
                3, "Sarcofago etrusco", "Anonimo", "s. III a.C.",
                95_000_000.0,
                LocalDate.of(1750, 1, 1),
                LocalDate.of(1998, 11, 20),
                EstadosMuseo.EXPUESTA,
                "pieza arqueologica"
        );

        encargado.registrarObra(cuadro);
        encargado.registrarObra(escultura);
        encargado.registrarObra(otro);
        encargado.asignarSala(cuadro, salaRenacimiento);
        encargado.asignarSala(escultura, salaModerna);
        encargado.asignarSala(otro, salaRenacimiento);

        mostrarCatalogoObras(catalogo);
        System.out.println();
        System.out.println("Catalogo actualizado el: " + catalogo.getFechaActualizacion());

        System.out.println();
        System.out.println("=== VISITANTE: consulta en monitor del vestibulo ===");
        ArrayList<Sala> salasMonitor = new ArrayList<>();
        salasMonitor.add(salaRenacimiento);
        salasMonitor.add(salaModerna);
        MonitorVestibulo monitor = new MonitorVestibulo(1, "Vestibulo principal", salasMonitor);
        Visitante visitante = new Visitante("por_sala", monitor);
        mostrarObrasEnSala(salaRenacimiento.getNombre(),
                visitante.consultarObrasPorSala(salaRenacimiento.getIdSala()));
        mostrarObrasEnSala(salaModerna.getNombre(),
                visitante.consultarObrasPorSala(salaModerna.getIdSala()));

        System.out.println();
        System.out.println("=== PROCESO DIARIO: RESTAURACION AUTOMATICA (cada 5 anos) ===");
        ProcesoRestauracionAutomatica proceso = new ProcesoRestauracionAutomatica(catalogo);
        List<ObraArte> candidatas = proceso.identificarObrasParaRestauracion();
        System.out.println("Obras candidatas a revision periodica hoy (" + LocalDate.now() + "): "
                + candidatas.size());
        for (ObraArte o : candidatas) {
            System.out.println("  - ID " + o.getIdObra() + " - " + o.getTitulo()
                    + " (referencia ciclo desde " + o.fechaReferenciaProximoCicloRestauracion() + ")");
        }

        System.out.println();
        System.out.println("=== RESTAURADOR JEFE: restauraciones ===");
        restaurador.iniciarSesion("res2024");
        cuadro.estado = EstadosMuseo.DANADA;
        Restauracion restUrgente = restaurador.iniciarRestauracion(
                cuadro, "consolidacion de capa pictorica", LocalDate.now(),
                "dano por humedad en sala (envio inmediato)"
        );
        System.out.println("Restauracion urgente #" + restUrgente.getIdRestauracion()
                + " iniciada | obra en estado: " + cuadro.getEstado());
        restaurador.finalizarRestauracion(restUrgente);
        System.out.println("Restauracion #" + restUrgente.getIdRestauracion() + " finalizada | fin: "
                + restUrgente.getFechaFin().orElse(null) + " | obra: " + cuadro.getEstado());

        Restauracion restCiclo = restaurador.iniciarRestauracion(
                escultura, "limpieza y patina", LocalDate.now(), "mantenimiento periodico"
        );
        restaurador.finalizarRestauracion(restCiclo);

        mostrarRestauracionesDeObra(cuadro, restaurador.consultarRestauracionesPorObra(cuadro));
        System.out.println();
        System.out.println("--- Todas las restauraciones gestionadas por el jefe (por antiguedad) ---");
        for (Restauracion r : restaurador.consultarRestauraciones()) {
            System.out.println("  - #" + r.getIdRestauracion() + " | obra ID " + r.getObra().getIdObra()
                    + " | " + r.getTipoRestauracion() + " | " + r.getFechaInicio());
        }

        System.out.println();
        System.out.println("=== DIRECTOR: museos colaboradores y cesiones ===");
        director.iniciarSesion("dir2024");
        MuseoColaborador museoBogota = new MuseoColaborador(1, "Museo Nacional de Colombia", "Bogota", "Colombia");
        MuseoColaborador museoMadrid = new MuseoColaborador(2, "Museo Thyssen-Bornemisza", "Madrid", "Espana");
        director.registrarMuseoColaborador(museoBogota);
        director.registrarMuseoColaborador(museoMadrid);
        System.out.println("Museos colaboradores registrados: " + director.getMuseosColaboradores().size());

        LocalDate inicioCesion = LocalDate.now();
        LocalDate finCesion = inicioCesion.plusDays(180);
        Optional<Cesion> optCesion1 = director.cederObra(otro, museoBogota, inicioCesion, finCesion, 12_500_000.0);
        if (!optCesion1.isPresent()) {
            throw new IllegalStateException("cesion esperada");
        }
        Cesion cesion1 = optCesion1.get();
        System.out.println("Cesion #" + cesion1.getIdCesion() + " | obra '" + otro.getTitulo()
                + "' -> " + museoBogota.getNombre()
                + " | importe $" + String.format("%,.0f", cesion1.getImportePagado())
                + " | estado obra: " + otro.getEstado());

        Optional<Cesion> cesionEnCola = director.cederObra(otro, museoMadrid, inicioCesion, finCesion, 0.0);
        if (!cesionEnCola.isPresent()) {
            System.out.println("Segundo museo solicito la misma obra mientras sigue cedida: cola "
                    + otro.colaSolicitudesCesion.size() + " pendiente(s).");
        }

        Optional<Cesion> cesionSiguiente = director.finalizarCesionYAsignarSiguiente(cesion1);
        if (cesionSiguiente.isPresent()) {
            Cesion c = cesionSiguiente.get();
            System.out.println("Tras finalizar la primera cesion: nueva cesion #" + c.getIdCesion()
                    + " -> " + c.getMuseo().getNombre() + " | estado: " + c.getEstado());
        } else {
            System.out.println("No hubo cesion siguiente (cola vacia o obra no disponible).");
        }

        System.out.println();
        System.out.println("=== VALORACION TOTAL DEL MUSEO ===");
        System.out.println("Suma de valores de todas las obras: $"
                + String.format("%,.0f", director.consultarValorTotal()));

        encargado.cerrarSesion();
        director.cerrarSesion();
        restaurador.cerrarSesion();
        System.out.println();
        System.out.println("=== FIN DE LA SIMULACION ===");
        System.out.println();
    }

    private static void mostrarCatalogoObras(CatalogoObras catalogo) {
        System.out.println();
        System.out.println("=== CATALOGO DE OBRAS ===");
        for (ObraArte obra : catalogo.listarObras()) {
            System.out.println("ID " + obra.getIdObra() + " | " + obra.getTitulo() + " | "
                    + obra.getAutor() + " | " + obra.getPeriodo()
                    + " | Valor: $" + String.format("%,.0f", obra.getValorEconomico())
                    + " | Estado: " + obra.getEstado());
            System.out.println("    " + obra.mostrarDetalle());
        }
    }

    private static void mostrarObrasEnSala(String nombreSala, List<ObraArte> obras) {
        System.out.println();
        System.out.println("--- Obras en sala: " + nombreSala + " ---");
        if (obras.isEmpty()) {
            System.out.println("  (ninguna)");
            return;
        }
        for (ObraArte obra : obras) {
            System.out.println("  - " + obra.getTitulo() + " - " + obra.getAutor());
        }
    }

    private static void mostrarRestauracionesDeObra(ObraArte obra, List<Restauracion> lista) {
        System.out.println();
        System.out.println("--- Restauraciones de '" + obra.getTitulo() + "' (por antiguedad) ---");
        if (lista.isEmpty()) {
            System.out.println("  (ninguna registrada)");
            return;
        }
        for (Restauracion r : lista) {
            String fin = r.getFechaFin().map(Object::toString).orElse("en curso");
            System.out.println("  - #" + r.getIdRestauracion() + " | " + r.getTipoRestauracion()
                    + " | inicio " + r.getFechaInicio() + " | fin " + fin + " | motivo: " + r.getMotivo());
        }
    }
}
