const estadosData = [
  { id: "agu", name: "Aguascalientes",  poblacion: 1500000 },
  { id: "bcn", name: "Baja California",  poblacion: 3800000 },
  { id: "bcs", name: "Baja California Sur",  poblacion: 800000 },
  { id: "cam", name: "Campeche",  poblacion: 928000 },
  { id: "chp", name: "Chiapas",  poblacion: 5500000 },
  { id: "chh", name: "Chihuahua",  poblacion: 3800000 },
  { id: "coa", name: "Coahuila",  poblacion: 3200000 },
  { id: "col", name: "Colima",  poblacion: 731000 },
  { id: "dur", name: "Durango",  poblacion: 1830000 },
  { id: "gua", name: "Guanajuato",  poblacion: 6200000 },
  { id: "gro", name: "Guerrero",  poblacion: 3540000 },
  { id: "hid", name: "Hidalgo",  poblacion: 3080000 },
  { id: "jal", name: "Jalisco",  poblacion: 8300000 },
  { id: "cmx", name: "Mexico City",  poblacion: 9200000 },
  { id: "mex", name: "México",  poblacion: 17000000 },
  { id: "mic", name: "Michoacán",  poblacion: 4700000 },
  { id: "mor", name: "Morelos",  poblacion: 1970000 },
  { id: "nay", name: "Nayarit",  poblacion: 1250000 },
  { id: "nle", name: "Nuevo León",  poblacion: 5800000 },
  { id: "oax", name: "Oaxaca",  poblacion: 4100000 },
  { id: "pue", name: "Puebla",  poblacion: 6600000 },
  { id: "que", name: "Querétaro",  poblacion: 2400000 },
  { id: "roo", name: "Quintana Roo",  poblacion: 1850000 },
  { id: "slp", name: "San Luis Potosí",  poblacion: 2800000 },
  { id: "sin", name: "Sinaloa",  poblacion: 3000000 },
  { id: "son", name: "Sonora",  poblacion: 3000000 },
  { id: "tab", name: "Tabasco",  poblacion: 2400000 },
  { id: "tam", name: "Tamaulipas",  poblacion: 3500000 },
  { id: "tla", name: "Tlaxcala",  poblacion: 1350000 },
  { id: "ver", name: "Veracruz",  poblacion: 8100000 },
  { id: "yuc", name: "Yucatán",  poblacion: 2200000 },
  { id: "zac", name: "Zacatecas",  poblacion: 1620000 },
];

// Coastal/gulf states for naval transport (approximate)
const costeros = new Set([
  "bcn", "bcs", "son", "sin", "nay", "jal", "col", "mic",
  "gro", "oax", "chp", "tam", "ver", "tab", "cam", "yuc", "roo"
]);

function generarValores(semilla) {
  const s = (semilla * 7 + 13) % 101;
  return {
    agua: 40 + ((s * 3) % 51),
    energia: 40 + ((s * 5) % 51),
    alimento: 40 + ((s * 7) % 51),
    salud: 35 + ((s * 11) % 51),
    sostenibilidad: 45 + ((s * 13) % 41),
    infraestructura: 25 + ((s * 17) % 46),
    desarrolloSociocultural: 30 + ((s * 19) % 46),
    distribucion: 20 + ((s * 23) % 46),
  };
}

function calcBienestar(recursos) {
  const vals = Object.values(recursos);
  return Math.round(vals.reduce((a, b) => a + b, 0) / vals.length);
}

const estadosInicial = {};
let contador = 0;

for (const e of estadosData) {
  const recursos = generarValores(contador);
  const bienestar = calcBienestar(recursos);
  estadosInicial[e.id] = {
    nombre: e.name,
    poblacion: e.poblacion,
    bienestar,
    presupuesto: 40 + ((contador * 17) % 51),
    recursos,
    infraestructura: {
      vivienda: 1,
      industria: 1,
      transporte: {
        aereo: true,
        terrestre: true,
        naval: costeros.has(e.id),
      },
    },
  };
  contador++;
}

function calcularFederales(estados) {
  const values = Object.values(estados);
  const total = values.length;
  if (total === 0) return { poblacionTotal: 0, bienestarFederal: 0, presupuestoFederal: 0, aguaPromedio: 0, energiaPromedio: 0, alimentoPromedio: 0, saludPromedio: 0, sostenibilidadPromedio: 0, infraestructuraPromedio: 0, desarrolloPromedio: 0, distribucionPromedio: 0 };
  const sum = (k) => values.reduce((s, e) => s + (typeof e.recursos[k] === 'number' ? e.recursos[k] : e[k] || 0), 0);
  return {
    poblacionTotal: values.reduce((s, e) => s + e.poblacion, 0),
    bienestarFederal: Math.round(values.reduce((s, e) => s + e.bienestar, 0) / total),
    presupuestoFederal: values.reduce((s, e) => s + e.presupuesto, 0),
    aguaPromedio: Math.round(sum('agua') / total),
    energiaPromedio: Math.round(sum('energia') / total),
    alimentoPromedio: Math.round(sum('alimento') / total),
    saludPromedio: Math.round(sum('salud') / total),
    sostenibilidadPromedio: Math.round(sum('sostenibilidad') / total),
    infraestructuraPromedio: Math.round(sum('infraestructura') / total),
    desarrolloPromedio: Math.round(sum('desarrolloSociocultural') / total),
    distribucionPromedio: Math.round(sum('distribucion') / total),
  };
}

export { estadosInicial, calcularFederales };
