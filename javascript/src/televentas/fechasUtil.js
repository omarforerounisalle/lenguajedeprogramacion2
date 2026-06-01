/** Fecha civil “hoy” en UTC (medianoche). */
export function hoyUTC() {
  const n = new Date();
  return new Date(Date.UTC(n.getUTCFullYear(), n.getUTCMonth(), n.getUTCDate()));
}
