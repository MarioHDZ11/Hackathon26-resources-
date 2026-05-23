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
      const { estado, recurso, cantidad } = action.payload;
      const nuevos = copiarEstado(state.estados);
      const e = nuevos[estado];
      const costoPresupuesto = cantidad * 3;
      if (e.presupuesto < costoPresupuesto) return state;
      e.presupuesto -= costoPresupuesto;
      if (recurso === 'Agua') {
        e.recursos.agua = Math.min(100, e.recursos.agua + cantidad);
        e.recursos.sostenibilidad = Math.max(0, e.recursos.sostenibilidad - cantidad * 0.2);
      } else if (recurso === 'Energía') {
        e.recursos.energia = Math.min(100, e.recursos.energia + cantidad);
        e.recursos.sostenibilidad = Math.max(0, e.recursos.sostenibilidad - cantidad * 0.25);
      } else if (recurso === 'Presupuesto') {
        // Build industry to generate ongoing budget
        e.infraestructura.industria += 1;
        e.recursos.sostenibilidad = Math.max(0, e.recursos.sostenibilidad - cantidad * 0.15);
        e.recursos.energia = Math.max(0, e.recursos.energia - cantidad * 0.1);
        e.recursos.agua = Math.max(0, e.recursos.agua - cantidad * 0.1);
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
