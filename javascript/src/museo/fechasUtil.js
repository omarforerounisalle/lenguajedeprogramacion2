/** Fecha civil en UTC (medianoche). */
export function fechaUTC(year, month, day) {
  return new Date(Date.UTC(year, month - 1, day));
}

export function hoyUTC() {
  const n = new Date();
  return new Date(Date.UTC(n.getUTCFullYear(), n.getUTCMonth(), n.getUTCDate()));
}

export function agregarAnios(fecha, anios) {
  const d = new Date(fecha.getTime());
  d.setUTCFullYear(d.getUTCFullYear() + anios);
  return d;
}

export function sumarDias(fecha, dias) {
  const d = new Date(fecha.getTime());
  d.setUTCDate(d.getUTCDate() + dias);
  return d;
}

/** A >= B comparando solo dia UTC */
export function esAntesOIgual(fechaA, fechaB) {
  return fechaA.getTime() <= fechaB.getTime();
}

export function esAntes(fechaA, fechaB) {
  return fechaA.getTime() < fechaB.getTime();
}

/** Anos completos entre dos fechas civiles UTC (similar a ChronoUnit.YEARS). */
export function anosEntre(inicio, fin) {
  let y = fin.getUTCFullYear() - inicio.getUTCFullYear();
  const m = fin.getUTCMonth() - inicio.getUTCMonth();
  const d = fin.getUTCDate() - inicio.getUTCDate();
  if (m < 0 || (m === 0 && d < 0)) {
    y -= 1;
  }
  return Math.max(0, y);
}

export function diasEntre(inicio, fin) {
  const ms = 86400000;
  const a = Date.UTC(inicio.getUTCFullYear(), inicio.getUTCMonth(), inicio.getUTCDate());
  const b = Date.UTC(fin.getUTCFullYear(), fin.getUTCMonth(), fin.getUTCDate());
  return Math.round((b - a) / ms);
}
