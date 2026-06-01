import * as Estados from "./estados.js";
import { fechaUTC, hoyUTC, sumarDias } from "./fechasUtil.js";
import { CatalogoObras } from "./catalogoObras.js";
import { Sala } from "./sala.js";
import { EncargadoCatalogo } from "./encargadoCatalogo.js";
import { Director } from "./director.js";
import { RestauradorJefe } from "./restauradorJefe.js";
import { Cuadro } from "./cuadro.js";
import { Escultura } from "./escultura.js";
import { OtroObjeto } from "./otroObjeto.js";
import { MonitorVestibulo } from "./monitorVestibulo.js";
import { Visitante } from "./visitante.js";
import { ProcesoRestauracionAutomatica } from "./procesoRestauracionAutomatica.js";
import { MuseoColaborador } from "./museoColaborador.js";

function fmtMoney(n) {
  return n.toLocaleString("en-US", { maximumFractionDigits: 0 });
}

function fmtDate(d) {
  if (d == null) return String(d);
  return d.toISOString().slice(0, 10);
}

function mostrarCatalogoObras(catalogo) {
  console.log();
  console.log("=== CATALOGO DE OBRAS ===");
  for (const obra of catalogo.listarObras()) {
    console.log(
      `ID ${obra.getIdObra()} | ${obra.getTitulo()} | ${obra.getAutor()} | ${obra.getPeriodo()} | Valor: $${fmtMoney(obra.getValorEconomico())} | Estado: ${obra.getEstado()}`
    );
    console.log(`    ${obra.mostrarDetalle()}`);
  }
}

function mostrarObrasEnSala(nombreSala, obras) {
  console.log();
  console.log(`--- Obras en sala: ${nombreSala} ---`);
  if (obras.length === 0) {
    console.log("  (ninguna)");
    return;
  }
  for (const obra of obras) {
    console.log(`  - ${obra.getTitulo()} - ${obra.getAutor()}`);
  }
}

function mostrarRestauracionesDeObra(obra, lista) {
  console.log();
  console.log(`--- Restauraciones de '${obra.getTitulo()}' (por antiguedad) ---`);
  if (lista.length === 0) {
    console.log("  (ninguna registrada)");
    return;
  }
  for (const r of lista) {
    const fin = r.getFechaFin() != null ? fmtDate(r.getFechaFin()) : "en curso";
    console.log(
      `  - #${r.getIdRestauracion()} | ${r.getTipoRestauracion()} | inicio ${fmtDate(r.getFechaInicio())} | fin ${fin} | motivo: ${r.getMotivo()}`
    );
  }
}

function main() {
  console.log();
  console.log("============================================================");
  console.log("SIMULACION: GESTION DEL MUSEO (JavaScript)");
  console.log("============================================================");

  const catalogo = new CatalogoObras();
  const salaRenacimiento = new Sala(1, "Renacimiento", "Planta 1 - Ala norte");
  const salaModerna = new Sala(2, "Arte moderno", "Planta 2");

  const encargado = new EncargadoCatalogo(
    1,
    "Ana Martinez",
    "ana@museo.org",
    true,
    "cat2024",
    "EC-001",
    catalogo
  );

  const director = new Director(
    2,
    "Luis Herrera",
    "director@museo.org",
    true,
    "dir2024",
    "Director general",
    catalogo
  );

  const restaurador = new RestauradorJefe(
    3,
    "Elena Vargas",
    "restauracion@museo.org",
    true,
    "res2024",
    "Pintura sobre lienzo"
  );

  console.log();
  console.log("=== INTENTO SIN AUTENTICACION (encargado del catalogo) ===");
  const cuadro = new Cuadro(
    1,
    "Noche estrellada",
    "Vincent van Gogh",
    "s. XIX",
    850_000_000.0,
    fechaUTC(1889, 6, 1),
    fechaUTC(2010, 3, 15),
    Estados.EXPUESTA,
    "oleo sobre lienzo",
    "postimpresionismo"
  );
  try {
    encargado.registrarObra(cuadro);
  } catch (e) {
    console.log(`Rechazado (correcto): ${e.message}`);
  }

  console.log();
  console.log("=== ENCARGADO DEL CATALOGO: sesion y registro de obras ===");
  encargado.iniciarSesion("cat2024");
  console.log(`Sesion iniciada: ${encargado.obtenerDatos()}`);

  const escultura = new Escultura(
    2,
    "El pensador",
    "Auguste Rodin",
    "s. XIX",
    420_000_000.0,
    fechaUTC(1902, 1, 1),
    fechaUTC(2005, 6, 1),
    Estados.EXPUESTA,
    "bronce",
    "modernista"
  );
  const otro = new OtroObjeto(
    3,
    "Sarcofago etrusco",
    "Anonimo",
    "s. III a.C.",
    95_000_000.0,
    fechaUTC(1750, 1, 1),
    fechaUTC(1998, 11, 20),
    Estados.EXPUESTA,
    "pieza arqueologica"
  );

  encargado.registrarObra(cuadro);
  encargado.registrarObra(escultura);
  encargado.registrarObra(otro);
  encargado.asignarSala(cuadro, salaRenacimiento);
  encargado.asignarSala(escultura, salaModerna);
  encargado.asignarSala(otro, salaRenacimiento);

  mostrarCatalogoObras(catalogo);
  console.log();
  console.log(`Catalogo actualizado el: ${fmtDate(catalogo.getFechaActualizacion())}`);

  console.log();
  console.log("=== VISITANTE: consulta en monitor del vestibulo ===");
  const salasMonitor = [salaRenacimiento, salaModerna];
  const monitor = new MonitorVestibulo(1, "Vestibulo principal", salasMonitor);
  const visitante = new Visitante("por_sala", monitor);
  mostrarObrasEnSala(salaRenacimiento.getNombre(), visitante.consultarObrasPorSala(salaRenacimiento.getIdSala()));
  mostrarObrasEnSala(salaModerna.getNombre(), visitante.consultarObrasPorSala(salaModerna.getIdSala()));

  console.log();
  console.log("=== PROCESO DIARIO: RESTAURACION AUTOMATICA (cada 5 anos) ===");
  const proceso = new ProcesoRestauracionAutomatica(catalogo);
  const hoy = hoyUTC();
  const candidatas = proceso.identificarObrasParaRestauracion(hoy);
  console.log(`Obras candidatas a revision periodica hoy (${fmtDate(hoy)}): ${candidatas.length}`);
  for (const o of candidatas) {
    console.log(
      `  - ID ${o.getIdObra()} - ${o.getTitulo()} (referencia ciclo desde ${fmtDate(o.fechaReferenciaProximoCicloRestauracion())})`
    );
  }

  console.log();
  console.log("=== RESTAURADOR JEFE: restauraciones ===");
  restaurador.iniciarSesion("res2024");
  cuadro.estado = Estados.DANADA;
  const restUrgente = restaurador.iniciarRestauracion(
    cuadro,
    "consolidacion de capa pictorica",
    hoy,
    "dano por humedad en sala (envio inmediato)"
  );
  console.log(
    `Restauracion urgente #${restUrgente.getIdRestauracion()} iniciada | obra en estado: ${cuadro.getEstado()}`
  );
  restaurador.finalizarRestauracion(restUrgente);
  console.log(
    `Restauracion #${restUrgente.getIdRestauracion()} finalizada | fin: ${restUrgente.getFechaFin() != null ? fmtDate(restUrgente.getFechaFin()) : null} | obra: ${cuadro.getEstado()}`
  );

  const restCiclo = restaurador.iniciarRestauracion(escultura, "limpieza y patina", hoy, "mantenimiento periodico");
  restaurador.finalizarRestauracion(restCiclo);

  mostrarRestauracionesDeObra(cuadro, restaurador.consultarRestauracionesPorObra(cuadro));
  console.log();
  console.log("--- Todas las restauraciones gestionadas por el jefe (por antiguedad) ---");
  for (const r of restaurador.consultarRestauraciones()) {
    console.log(
      `  - #${r.getIdRestauracion()} | obra ID ${r.getObra().getIdObra()} | ${r.getTipoRestauracion()} | ${fmtDate(r.getFechaInicio())}`
    );
  }

  console.log();
  console.log("=== DIRECTOR: museos colaboradores y cesiones ===");
  director.iniciarSesion("dir2024");
  const museoBogota = new MuseoColaborador(1, "Museo Nacional de Colombia", "Bogota", "Colombia");
  const museoMadrid = new MuseoColaborador(2, "Museo Thyssen-Bornemisza", "Madrid", "Espana");
  director.registrarMuseoColaborador(museoBogota);
  director.registrarMuseoColaborador(museoMadrid);
  console.log(`Museos colaboradores registrados: ${director.getMuseosColaboradores().length}`);

  const inicioCesion = hoyUTC();
  const finCesion = sumarDias(inicioCesion, 180);

  const cesion1 = director.cederObra(otro, museoBogota, inicioCesion, finCesion, 12_500_000.0);
  if (cesion1 == null) {
    throw new Error("cesion esperada");
  }
  console.log(
    `Cesion #${cesion1.getIdCesion()} | obra '${otro.getTitulo()}' -> ${museoBogota.getNombre()} | importe $${fmtMoney(cesion1.getImportePagado())} | estado obra: ${otro.getEstado()}`
  );

  const cesionEnCola = director.cederObra(otro, museoMadrid, inicioCesion, finCesion, 0.0);
  if (cesionEnCola == null) {
    console.log(`Segundo museo solicito la misma obra mientras sigue cedida: cola ${otro.colaSolicitudesCesion.length} pendiente(s).`);
  }

  const cesionSiguiente = director.finalizarCesionYAsignarSiguiente(cesion1);
  if (cesionSiguiente != null) {
    const c = cesionSiguiente;
    console.log(
      `Tras finalizar la primera cesion: nueva cesion #${c.getIdCesion()} -> ${c.getMuseo().getNombre()} | estado: ${c.getEstado()}`
    );
  } else {
    console.log("No hubo cesion siguiente (cola vacia o obra no disponible).");
  }

  console.log();
  console.log("=== VALORACION TOTAL DEL MUSEO ===");
  console.log(`Suma de valores de todas las obras: $${fmtMoney(director.consultarValorTotal())}`);

  encargado.cerrarSesion();
  director.cerrarSesion();
  restaurador.cerrarSesion();
  console.log();
  console.log("=== FIN DE LA SIMULACION ===");
  console.log();
}

main();
