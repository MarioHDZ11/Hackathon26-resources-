import React, { createContext, useContext, useReducer, useEffect, useRef } from 'react';
import { estadosInicial, calcularFederales } from '../data/initialState';

const GameStateContext = createContext(null);

const FACTOR_CONSUMO = 0.00000015;
const TICK_INTERVAL = 10000;

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
  return { estados, ...federal, tickCount: 0 };
}

function gameReducer(state, action) {
  switch (action.type) {
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
      return { estados: nuevos, ...federal, tickCount: state.tickCount + 1 };
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
      return { estados: nuevos, ...federal, tickCount: state.tickCount };
    }
    default:
      return state;
  }
}

export function GameStateProvider({ children }) {
  const [state, dispatch] = useReducer(gameReducer, null, initialState);
  const tickRef = useRef(null);

  useEffect(() => {
    tickRef.current = setInterval(() => {
      dispatch({ type: 'TICK' });
    }, TICK_INTERVAL);
    return () => clearInterval(tickRef.current);
  }, []);

  return (
    <GameStateContext.Provider value={{ state, dispatch }}>
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
