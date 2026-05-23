import React, { createContext, useContext, useReducer, useEffect, useRef, useCallback } from 'react';
import { estadosInicial, calcularFederales } from '../data/initialState';
import api from '../services/api';

const INIT_DATA = estadosInicial;
const INIT_KEYS = Object.keys(INIT_DATA);

const GameStateContext = createContext(null);

const FACTOR_CONSUMO = 0.00000015;
const TICK_INTERVAL = 10000;
const SYNC_INTERVAL = 30000;

function copiarEstado(estados) {
  const copia = {};
  for (const id in estados) {
    copia[id] = {
      ...estados[id],
      recursos: { ...estados[id].recursos },
      infraestructura: {
        ...estados[id].infraestructura,
        transporte: { ...estados[id].infraestructura.transporte },
      },
    };
  }
  return copia;
}

function recalcBienestar(recursos) {
  const vals = Object.values(recursos);
  return Math.round(vals.reduce((a, b) => a + b, 0) / vals.length);
}

function initialState() {
  const estados = copiarEstado(estadosInicial);
  const federal = calcularFederales(estados);
  return { estados, ...federal, tickCount: 0, idPartida: null };
}

function serializarEstado(state) {
  const estadosArr = Object.entries(state.estados).map(([id, e], idx) => ({
    idEstadoRef: idx + 1,
    nombreEstado: e.nombre,
    poblacion: e.poblacion,
    bienestar: e.bienestar,
    presupuesto: e.presupuesto,
    agua: e.recursos.agua,
    energia: e.recursos.energia,
    alimento: e.recursos.alimento,
    salud: e.recursos.salud,
    sostenibilidad: e.recursos.sostenibilidad,
    infraestructura: e.recursos.infraestructura,
    desarrolloSociocultural: e.recursos.desarrolloSociocultural,
    distribucion: e.recursos.distribucion,
  }));
  return {
    tickCount: state.tickCount,
    estados: estadosArr,
    presupuestoFederal: state.presupuestoFederal,
    poblacionTotal: state.poblacionTotal,
    bienestarFederal: state.bienestarFederal,
  };
}

function gameReducer(state, action) {
  switch (action.type) {
    case 'CARGAR_ESTADO': {
      return { ...action.payload };
    }
    case 'SET_ID_PARTIDA': {
      return { ...state, idPartida: action.payload };
    }
    case 'TICK': {
      const nuevos = copiarEstado(state.estados);
      for (const id in nuevos) {
        const e = nuevos[id];
        const consumo = Math.min(e.poblacion * FACTOR_CONSUMO, 1.5);
        e.recursos.agua = Math.max(0, +(e.recursos.agua - consumo).toFixed(2));
        e.recursos.energia = Math.max(0, +(e.recursos.energia - consumo).toFixed(2));
        e.recursos.alimento = Math.max(0, +(e.recursos.alimento - consumo).toFixed(2));
        e.bienestar = recalcBienestar(e.recursos);
        const delta = (e.bienestar - 50) / 5000;
        e.poblacion = Math.max(100000, Math.round(e.poblacion * (1 + delta)));
      }
      const federal = calcularFederales(nuevos);
      return { ...state, estados: nuevos, ...federal, tickCount: state.tickCount + 1 };
    }
    case 'INVERTIR_RECURSO': {
      const { estado, recurso, transporte } = action.payload;
      const nuevos = copiarEstado(state.estados);
      const e = nuevos[estado];

      const reglas = {
        'Agua': {
          consume: { Presupuesto: 30, Sostenibilidad: 2 },
          genera: { Agua: 15 },
        },
        'Energía': {
          consume: { Presupuesto: 30, Sostenibilidad: 3 },
          genera: { Energía: 15 },
        },
        'Alimento': {
          consume: { Presupuesto: 40, Sostenibilidad: 2, Energía: 5, Agua: 5 },
          genera: { Alimento: 15 },
        },
        'Salud': {
          consume: { Presupuesto: 35, Energía: 3, Agua: 3 },
          genera: { Salud: 15 },
        },
        'Infraestructura': {
          consume: { Presupuesto: 50, Sostenibilidad: 3, Energía: 5, Agua: 5 },
          genera: { Presupuesto: 8 },
        },
        'D. Social y Cultural': {
          consume: { Presupuesto: 30, Sostenibilidad: 2, Energía: 3, Agua: 3 },
          genera: {},
        },
        'Sostenibilidad': {
          consume: { Presupuesto: 25, Energía: 3, Agua: 3 },
          genera: { Sostenibilidad: 15 },
        },
        'Distribución': {
          consume: { Presupuesto: 30, Sostenibilidad: 2 },
          genera: { Distribución: 12 },
          modos: {
            Aéreo: { costoPresupuesto: 1.5, beneficio: 1.3 },
            Terrestre: { costoPresupuesto: 1.0, beneficio: 1.0 },
            Naval: { costoPresupuesto: 0.7, beneficio: 0.8 },
          },
        },
      };

      const r = reglas[recurso];
      if (!r) return state;

      let multCosto = 1;
      let multBene = 1;
      if (recurso === 'Distribución' && transporte && r.modos[transporte]) {
        multCosto = r.modos[transporte].costoPresupuesto;
        multBene = r.modos[transporte].beneficio;
      }

      const costoPresupuesto = Math.round(r.consume.Presupuesto * multCosto);
      if (e.presupuesto < costoPresupuesto) return state;
      e.presupuesto -= costoPresupuesto;

      for (const [res, val] of Object.entries(r.consume)) {
        if (res === 'Presupuesto') continue;
        if (typeof e.recursos[res] === 'number') {
          e.recursos[res] = Math.max(0, +(e.recursos[res] - val * multCosto).toFixed(1));
        }
      }

      for (const [res, val] of Object.entries(r.genera)) {
        if (res === 'Presupuesto') {
          e.presupuesto += Math.round(val * multBene);
        } else if (typeof e.recursos[res] === 'number') {
          e.recursos[res] = Math.min(100, +(e.recursos[res] + val * multBene).toFixed(1));
        }
      }

      e.bienestar = recalcBienestar(e.recursos);
      const federal = calcularFederales(nuevos);
      return { ...state, estados: nuevos, ...federal, tickCount: state.tickCount };
    }
    default:
      return state;
  }
}

export function GameStateProvider({ children }) {
  const [state, dispatch] = useReducer(gameReducer, null, initialState);
  const tickRef = useRef(null);
  const syncRef = useRef(null);
  const stateRef = useRef(state);
  const inicializado = useRef(false);

  stateRef.current = state;

  const syncState = useCallback(async () => {
    try {
      const s = stateRef.current;
      if (!s.idPartida) return;
      const datosJson = JSON.stringify(serializarEstado(s));
      await api.actualizarPartida(s.idPartida, {
        tickCount: s.tickCount,
        datosJson,
        estadosPartida: Object.entries(s.estados).map(([id, e], idx) => ({
          idEstadoRef: idx + 1,
          nombreEstado: e.nombre,
          poblacion: e.poblacion,
          bienestar: e.bienestar,
          presupuesto: e.presupuesto,
          agua: e.recursos.agua,
          energia: e.recursos.energia,
          alimento: e.recursos.alimento,
          salud: e.recursos.salud,
          sostenibilidad: e.recursos.sostenibilidad,
          infraestructura: e.recursos.infraestructura,
          desarrolloSociocultural: e.recursos.desarrolloSociocultural,
          distribucion: e.recursos.distribucion,
        })),
      });
    } catch (err) {
      console.warn('Error syncing game state:', err.message);
    }
  }, []);

  useEffect(() => {
    if (inicializado.current) return;
    inicializado.current = true;

    api.getUltimaPartida().then((partida) => {
      if (partida && partida.datosJson) {
        try {
          const parsed = JSON.parse(partida.datosJson);
          if (parsed && parsed.estados) {
            const estadosMap = {};
            INIT_KEYS.forEach((key, idx) => {
              const ep = parsed.estados[idx];
              if (ep) {
                estadosMap[key] = {
                  ...INIT_DATA[key],
                  poblacion: ep.poblacion || INIT_DATA[key].poblacion,
                  bienestar: ep.bienestar || INIT_DATA[key].bienestar,
                  presupuesto: ep.presupuesto || INIT_DATA[key].presupuesto,
                  recursos: {
                    agua: ep.agua ?? INIT_DATA[key].recursos.agua,
                    energia: ep.energia ?? INIT_DATA[key].recursos.energia,
                    alimento: ep.alimento ?? INIT_DATA[key].recursos.alimento,
                    salud: ep.salud ?? INIT_DATA[key].recursos.salud,
                    sostenibilidad: ep.sostenibilidad ?? INIT_DATA[key].recursos.sostenibilidad,
                    infraestructura: ep.infraestructura ?? INIT_DATA[key].recursos.infraestructura,
                    desarrolloSociocultural: ep.desarrolloSociocultural ?? INIT_DATA[key].recursos.desarrolloSociocultural,
                    distribucion: ep.distribucion ?? INIT_DATA[key].recursos.distribucion,
                  },
                };
              }
            });
            const federal = calcularFederales(estadosMap);
            dispatch({
              type: 'CARGAR_ESTADO',
              payload: {
                estados: estadosMap,
                ...federal,
                tickCount: parsed.tickCount || 0,
                idPartida: partida.idPartida,
              },
            });
            return;
          }
        } catch (e) {
          console.warn('Error parsing saved game state, starting fresh');
        }
      }
      if (partida && partida.idPartida) {
        dispatch({ type: 'SET_ID_PARTIDA', payload: partida.idPartida });
      }
    }).catch(() => {
      api.crearPartida({
        nombre: 'Partida ' + new Date().toLocaleString(),
        datosJson: JSON.stringify(serializarEstado(stateRef.current)),
        estadosPartida: Object.entries(stateRef.current.estados).map(([id, e], idx) => ({
          idEstadoRef: idx + 1,
          nombreEstado: e.nombre,
          poblacion: e.poblacion,
          bienestar: e.bienestar,
          presupuesto: e.presupuesto,
          agua: e.recursos.agua,
          energia: e.recursos.energia,
          alimento: e.recursos.alimento,
          salud: e.recursos.salud,
          sostenibilidad: e.recursos.sostenibilidad,
          infraestructura: e.recursos.infraestructura,
          desarrolloSociocultural: e.recursos.desarrolloSociocultural,
          distribucion: e.recursos.distribucion,
        })),
      }).then((nueva) => {
        if (nueva && nueva.idPartida) {
          dispatch({ type: 'SET_ID_PARTIDA', payload: nueva.idPartida });
        }
      }).catch((err) => {
        console.warn('Could not create game on backend:', err.message);
      });
    });
  }, []);

  useEffect(() => {
    tickRef.current = setInterval(() => {
      dispatch({ type: 'TICK' });
    }, TICK_INTERVAL);
    return () => clearInterval(tickRef.current);
  }, []);

  useEffect(() => {
    syncRef.current = setInterval(() => {
      syncState();
    }, SYNC_INTERVAL);
    return () => clearInterval(syncRef.current);
  }, [syncState]);

  return (
    <GameStateContext.Provider value={{ state, dispatch, syncState }}>
      {children}
    </GameStateContext.Provider>
  );
}

export function useGameState() {
  const ctx = useContext(GameStateContext);
  if (!ctx) throw new Error('useGameState must be used within GameStateProvider');
  return ctx.state;
}

export function useGameDispatch() {
  const ctx = useContext(GameStateContext);
  if (!ctx) throw new Error('useGameDispatch must be used within GameStateProvider');
  return ctx.dispatch;
}

export function useGameSync() {
  const ctx = useContext(GameStateContext);
  if (!ctx) throw new Error('useGameSync must be used within GameStateProvider');
  return ctx.syncState;
}
