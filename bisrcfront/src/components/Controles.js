import React, { useState } from 'react';
import { useGameState, useGameDispatch } from '../context/GameStateContext';

const entidades = [
  "CDMX", "Jalisco", "Nuevo León", "Gobierno Federal",
  "ONU", "Cruz Roja", "Comisión de Agua", "SEDENA"
];

function Controles({ estadoSeleccionado }) {
  const state = useGameState();
  const dispatch = useGameDispatch();
  const [modal, setModal] = useState(null);

  const [invertirRecurso, setInvertirRecurso] = useState("Agua");
  const [invertirCantidad, setInvertirCantidad] = useState(10);

  const [entidadMendigar, setEntidadMendigar] = useState("");
  const [nivelDesesperacion, setNivelDesesperacion] = useState(5);
  const [mensajeAuxilio, setMensajeAuxilio] = useState("");

  const eArr = Object.values(state.estados);
  const aguaFed = eArr.length ? Math.round(eArr.reduce((s, e) => s + e.recursos.agua, 0) / eArr.length) : 0;
  const energiaFed = eArr.length ? Math.round(eArr.reduce((s, e) => s + e.recursos.energia, 0) / eArr.length) : 0;
  const presupuestoFed = state.presupuestoFederal;

  const estadoData = estadoSeleccionado ? state.estados[Object.keys(state.estados).find(k => state.estados[k].nombre === estadoSeleccionado)] : null;

  function cerrarModal() {
    setModal(null);
  }

  function confirmarInversion() {
    if (!estadoSeleccionado || !estadoData) return;
    const id = Object.keys(state.estados).find(k => state.estados[k].nombre === estadoSeleccionado);
    if (!id) return;
    dispatch({
      type: 'INVERTIR_RECURSO',
      payload: { estado: id, recurso: invertirRecurso, cantidad: invertirCantidad }
    });
    cerrarModal();
  }

  return (
    <footer className="controles">
      <div className="controles-info">
        <span className="info-badge">RECURSOS FED: {aguaFed}% A · {energiaFed}% E · ${presupuestoFed}M P</span>
        <span className="info-badge">
          ZONA: {estadoSeleccionado ? estadoSeleccionado.toUpperCase() : 'SIN SELECCIÓN'}
        </span>
      </div>
      <div className="controles-botones">
        <button
          className="btn btn-primario"
          disabled={!estadoSeleccionado}
          onClick={() => setModal('invertir')}
        >
          INVERTIR RECURSO
        </button>
        <button
          className="btn btn-secundario"
          disabled={!estadoSeleccionado}
          onClick={() => setModal('analizar')}
        >
          ANALIZAR ZONA
        </button>
        <button
          className="btn btn-terciario"
          disabled={!estadoSeleccionado}
          onClick={() => setModal('reporte')}
        >
          GENERAR REPORTE
        </button>
      </div>

      {modal === 'invertir' && (
        <div className="modal-overlay" onClick={cerrarModal}>
          <div className="modal-ventana" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <span className="modal-icon">◈</span>
              <span className="modal-titulo">INVERTIR RECURSO</span>
              <button className="modal-cerrar" onClick={cerrarModal}>✕</button>
            </div>
            <div className="modal-cuerpo">
              <p className="modal-desc">
                Asigna recursos adicionales a <strong>{estadoSeleccionado}</strong>.
                {estadoData && <span> Presupuesto disponible: ${estadoData.presupuesto}M</span>}
              </p>
              <div className="modal-campo">
                <label className="modal-label">RECURSO A INVERTIR</label>
                <select
                  className="modal-select"
                  value={invertirRecurso}
                  onChange={(e) => setInvertirRecurso(e.target.value)}
                >
                  <option value="Agua">Agua (costo: 3/unidad)</option>
                  <option value="Energía">Energía (costo: 3/unidad)</option>
                  <option value="Presupuesto">Industria (genera presupuesto)</option>
                </select>
              </div>
              <div className="modal-campo">
                <label className="modal-label">CANTIDAD REQUERIDA</label>
                <input
                  className="modal-input"
                  type="number"
                  min={1}
                  max={Math.min(100, estadoData ? Math.floor(estadoData.presupuesto / 3) : 33)}
                  value={invertirCantidad}
                  onChange={(e) => setInvertirCantidad(Number(e.target.value))}
                />
              </div>
              <div className="modal-accion">
                <button className="btn btn-primario" onClick={confirmarInversion}>
                  CONFIRMAR INVERSIÓN
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {modal === 'analizar' && (
        <div className="modal-overlay" onClick={cerrarModal}>
          <div className="modal-ventana" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <span className="modal-icon">◈</span>
              <span className="modal-titulo">ANALIZAR ZONA</span>
              <button className="modal-cerrar" onClick={cerrarModal}>✕</button>
            </div>
            <div className="modal-cuerpo">
              <p className="modal-desc">
                Inventario de recursos para <strong>{estadoSeleccionado}</strong>.
              </p>
              {estadoData && (
                <div className="analisis-grid">
                  <div className="analisis-item">
                    <span className="analisis-label">AGUA</span>
                    <div className="analisis-bar-bg">
                      <div className="analisis-bar-fill" style={{ width: `${estadoData.recursos.agua}%` }} />
                    </div>
                    <span className="analisis-valor">{Math.round(estadoData.recursos.agua)}%</span>
                  </div>
                  <div className="analisis-item">
                    <span className="analisis-label">ENERGÍA</span>
                    <div className="analisis-bar-bg">
                      <div className="analisis-bar-fill" style={{ width: `${estadoData.recursos.energia}%`, background: '#ffea00' }} />
                    </div>
                    <span className="analisis-valor">{Math.round(estadoData.recursos.energia)}%</span>
                  </div>
                  <div className="analisis-item">
                    <span className="analisis-label">ALIMENTO</span>
                    <div className="analisis-bar-bg">
                      <div className="analisis-bar-fill" style={{ width: `${estadoData.recursos.alimento}%`, background: '#ff9100' }} />
                    </div>
                    <span className="analisis-valor">{Math.round(estadoData.recursos.alimento)}%</span>
                  </div>
                  <div className="analisis-item">
                    <span className="analisis-label">SALUD</span>
                    <div className="analisis-bar-bg">
                      <div className="analisis-bar-fill" style={{ width: `${estadoData.recursos.salud}%`, background: '#ff1744' }} />
                    </div>
                    <span className="analisis-valor">{Math.round(estadoData.recursos.salud)}%</span>
                  </div>
                  <div className="analisis-item">
                    <span className="analisis-label">SOSTENIBILIDAD</span>
                    <div className="analisis-bar-bg">
                      <div className="analisis-bar-fill" style={{ width: `${estadoData.recursos.sostenibilidad}%`, background: '#00e676' }} />
                    </div>
                    <span className="analisis-valor">{Math.round(estadoData.recursos.sostenibilidad)}%</span>
                  </div>
                  <div className="analisis-divider" />
                  <div className="analisis-item">
                    <span className="analisis-label">POBLACIÓN</span>
                    <span className="analisis-valor-grande">{(estadoData.poblacion / 1e6).toFixed(1)}M</span>
                  </div>
                  <div className="analisis-item">
                    <span className="analisis-label">BIENESTAR</span>
                    <span className="analisis-valor-grande">{estadoData.bienestar}%</span>
                  </div>
                  <div className="analisis-item">
                    <span className="analisis-label">PRESUPUESTO</span>
                    <span className="analisis-valor-grande">${estadoData.presupuesto}M</span>
                  </div>
                </div>
              )}
              <div className="modal-accion">
                <button className="btn btn-primario" onClick={cerrarModal}>
                  CERRAR
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {modal === 'reporte' && (
        <div className="modal-overlay" onClick={cerrarModal}>
          <div className="modal-ventana" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <span className="modal-icon">◈</span>
              <span className="modal-titulo">PEDIDO DE AUXILIO</span>
              <button className="modal-cerrar" onClick={cerrarModal}>✕</button>
            </div>
            <div className="modal-cuerpo">
              <p className="modal-desc">
                {estadoSeleccionado} necesita ayuda. Prepara tu señal de humo.
              </p>
              <div className="modal-campo">
                <label className="modal-label">ENTIDAD A MENDIGAR</label>
                <select
                  className="modal-select"
                  value={entidadMendigar}
                  onChange={(e) => setEntidadMendigar(e.target.value)}
                >
                  <option value="">-- SELECCIONA --</option>
                  {entidades.map((e) => (
                    <option key={e} value={e}>{e}</option>
                  ))}
                </select>
              </div>
              <div className="modal-campo">
                <label className="modal-label">
                  NIVEL DE DESESPERACIÓN ({nivelDesesperacion}/10)
                </label>
                <input
                  className="modal-rango"
                  type="range"
                  min={1}
                  max={10}
                  value={nivelDesesperacion}
                  onChange={(e) => setNivelDesesperacion(Number(e.target.value))}
                />
                <div className="desesperacion-labels">
                  <span>TRANQUILO</span>
                  <span>¡AUXILIO!</span>
                </div>
              </div>
              <div className="modal-campo">
                <label className="modal-label">MENSAJE DE AUXILIO</label>
                <textarea
                  className="modal-textarea"
                  rows={3}
                  placeholder="Ej: Se nos acabó el agua, manden latas..."
                  value={mensajeAuxilio}
                  onChange={(e) => setMensajeAuxilio(e.target.value)}
                />
              </div>
              <div className="modal-accion">
                <button className="btn btn-primario" onClick={cerrarModal}>
                  ENVIAR SEÑAL DE HUMO
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </footer>
  );
}

export default Controles;
