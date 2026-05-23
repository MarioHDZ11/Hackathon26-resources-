import React from 'react';
import { useGameState } from '../context/GameStateContext';
import { ReactComponent as LogoSimplificado } from '../assets/BISOURCE LOGO SIMPLIFICADO.svg';

function Header() {
  const state = useGameState();
  const estadosArr = Object.values(state.estados);

  const federal = {
    agua: estadosArr.length ? Math.round(estadosArr.reduce((s, e) => s + e.recursos.agua, 0) / estadosArr.length) : 0,
    energia: estadosArr.length ? Math.round(estadosArr.reduce((s, e) => s + e.recursos.energia, 0) / estadosArr.length) : 0,
    presupuesto: state.presupuestoFederal,
  };

  const recursos = [
    { id: 'agua', label: 'AGUA', icon: '💧', valor: federal.agua, color: '#4fc3f7' },
    { id: 'energia', label: 'ENERGÍA', icon: '⚡', valor: federal.energia, color: '#ffeb3b' },
    { id: 'presupuesto', label: 'PRESUPUESTO', icon: '₿', valor: Math.min(100, federal.presupuesto), color: '#66bb6a' },
  ];

  return (
    <header className="header">
      <div className="header-title">
        <LogoSimplificado className="header-logo" />
        <div className="header-titulo-wrap">
          <h1>BisourcesMX</h1>
          <span className="header-sub">Gestión Integral de Recursos</span>
        </div>
      </div>
      <div className="recursos">
        {recursos.map((r) => (
          <div key={r.id} className="recurso-card">
            <div className="recurso-header">
              <span className="recurso-icon">{r.icon}</span>
              <span className="recurso-label">{r.label}</span>
            </div>
            <div className="recurso-bar-bg">
              <div
                className="recurso-bar-fill"
                style={{ width: `${r.valor}%`, backgroundColor: r.color }}
              />
            </div>
            <span className="recurso-valor">{r.valor}%</span>
          </div>
        ))}
      </div>
    </header>
  );
}

export default Header;
