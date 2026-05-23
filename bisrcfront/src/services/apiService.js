import apiClient from './api';

export const estadoIdMapping = {
  agu: 1, bcn: 2, bcs: 3, cam: 4, chp: 5, chh: 6, coa: 7, col: 8,
  dur: 9, gua: 10, gro: 11, hid: 12, jal: 13, cmx: 14, mex: 15, mic: 16,
  mor: 17, nay: 18, nle: 19, oax: 20, pue: 21, que: 22, roo: 23, slp: 24,
  sin: 25, son: 26, tab: 27, tam: 28, tla: 29, ver: 30, yuc: 31, zac: 32,
};

const idToEstadoCode = Object.fromEntries(
  Object.entries(estadoIdMapping).map(([k, v]) => [v, k])
);

function frontendEstadoToDTO(code, estado) {
  return {
    idEstadoRef: estadoIdMapping[code],
    nombreEstado: estado.nombre,
    poblacion: estado.poblacion,
    bienestar: estado.bienestar,
    presupuesto: estado.presupuesto,
    agua: estado.recursos.agua,
    energia: estado.recursos.energia,
    alimento: estado.recursos.alimento,
    salud: estado.recursos.salud,
    sostenibilidad: estado.recursos.sostenibilidad,
    infraestructura: estado.recursos.infraestructura,
    desarrolloSociocultural: estado.recursos.desarrolloSociocultural,
    distribucion: estado.recursos.distribucion,
  };
}

function backendEstadosToFrontend(estadosPartida) {
  const estados = {};
  for (const ep of estadosPartida) {
    const code = idToEstadoCode[ep.idEstadoRef];
    if (!code) continue;
    estados[code] = {
      nombre: ep.nombreEstado,
      poblacion: ep.poblacion,
      bienestar: ep.bienestar,
      presupuesto: ep.presupuesto,
      recursos: {
        agua: ep.agua,
        energia: ep.energia,
        alimento: ep.alimento,
        salud: ep.salud,
        sostenibilidad: ep.sostenibilidad,
        infraestructura: ep.infraestructura,
        desarrolloSociocultural: ep.desarrolloSociocultural,
        distribucion: ep.distribucion,
      },
      infraestructura: {
        vivienda: 1,
        industria: 1,
        transporte: { aereo: true, terrestre: true, naval: false },
      },
    };
  }
  return estados;
}

export async function fetchEstadoDetalle(id) {
  try {
    const res = await apiClient.get(`/estados/${id}`);
    return res.data;
  } catch {
    return null;
  }
}

export async function fetchUltimaPartida() {
  try {
    const res = await apiClient.get('/partidas/ultima');
    if (res.status === 204 || !res.data) return null;
    return res.data;
  } catch {
    return null;
  }
}

export async function crearPartidaEnBackend(estados, tickCount = 0) {
  try {
    const estadosPartida = Object.entries(estados).map(([code, est]) =>
      frontendEstadoToDTO(code, est)
    );
    const res = await apiClient.post('/partidas', {
      nombre: 'Partida ' + new Date().toLocaleString(),
      activa: true,
      tickCount,
      estadosPartida,
    });
    return res.data;
  } catch {
    return null;
  }
}

export async function actualizarPartidaEnBackend(idPartida, estados, tickCount = 0) {
  try {
    const estadosPartida = Object.entries(estados).map(([code, est]) =>
      frontendEstadoToDTO(code, est)
    );
    const res = await apiClient.put(`/partidas/${idPartida}`, {
      tickCount,
      estadosPartida,
      activa: true,
    });
    return res.data;
  } catch {
    return null;
  }
}

export function partidaToFrontendState(partida) {
  const estados = backendEstadosToFrontend(partida.estadosPartida);
  return {
    estados,
    tickCount: partida.tickCount || 0,
    idPartida: partida.idPartida,
  };
}

export function partidaToFrontendFederales(partida) {
  const estados = backendEstadosToFrontend(partida.estadosPartida);
  return calcularFederalesDesdeEstados(estados);
}

function calcularFederalesDesdeEstados(estados) {
  const values = Object.values(estados);
  const total = values.length;
  if (total === 0) {
    return {
      poblacionTotal: 0, bienestarFederal: 0, presupuestoFederal: 0,
      aguaPromedio: 0, energiaPromedio: 0, alimentoPromedio: 0,
      saludPromedio: 0, sostenibilidadPromedio: 0,
      infraestructuraPromedio: 0, desarrolloPromedio: 0, distribucionPromedio: 0,
    };
  }
  const sum = (k) => values.reduce((s, e) => s + (
    typeof e.recursos[k] === 'number' ? e.recursos[k] : e[k] || 0
  ), 0);
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
