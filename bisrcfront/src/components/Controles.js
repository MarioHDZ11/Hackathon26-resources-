import React, { useState } from 'react';
import { useGameState, useGameDispatch } from '../context/GameStateContext';

const entidades = [
  "CDMX", "Jalisco", "Nuevo León", "Gobierno Federal",
  "ONU", "Cruz Roja", "Comisión de Agua", "SEDENA"
];

const costosBeneficios = {
  'Agua': {
    consume: { Presupuesto: 30, Sostenibilidad: 2 },
    genera: { Agua: 15, Bienestar: 2 },
  },
  'Energía': {
    consume: { Presupuesto: 30, Sostenibilidad: 3 },
    genera: { Energía: 15, Bienestar: 2 },
  },
  'Alimento': {
    consume: { Presupuesto: 40, Sostenibilidad: 2, Energía: 5, Agua: 5 },
    genera: { Alimento: 15, Bienestar: 2 },
  },
  'Salud': {
    consume: { Presupuesto: 35, Energía: 3, Agua: 3 },
    genera: { Salud: 15, Bienestar: 3 },
  },
  'Infraestructura': {
    consume: { Presupuesto: 50, Sostenibilidad: 3, Energía: 5, Agua: 5 },
    genera: { Presupuesto: 8, Bienestar: 3 },
  },
  'D. Social y Cultural': {
    consume: { Presupuesto: 30, Sostenibilidad: 2, Energía: 3, Agua: 3 },
    genera: { Bienestar: 6 },
  },
  'Sostenibilidad': {
    consume: { Presupuesto: 25, Energía: 3, Agua: 3 },
    genera: { Sostenibilidad: 15, Bienestar: 3 },
  },
  'Distribución': {
    consume: { Presupuesto: 30, Sostenibilidad: 2 },
    genera: { Distribución: 12, Bienestar: 2 },
    modos: {
      Aéreo: { costoPresupuesto: 1.5, beneficio: 1.3 },
      Terrestre: { costoPresupuesto: 1.0, beneficio: 1.0 },
      Naval: { costoPresupuesto: 0.7, beneficio: 0.8 },
    },
  },
};

const opcionesRecursos = Object.keys(costosBeneficios);

function Controles({ estadoSeleccionado }) {
  const state = useGameState();
  const dispatch = useGameDispatch();
  const [modal, setModal] = useState(null);

  const [invertirRecurso, setInvertirRecurso] = useState("Agua");
  const [transporteModo, setTransporteModo] = useState("Terrestre");

  const [entidadMendigar, setEntidadMendigar] = useState("");
  const [nivelDesesperacion, setNivelDesesperacion] = useState(5);
  const [mensajeAuxilio, setMensajeAuxilio] = useState("");

  const eArr = Object.values(state.estados);
  const aguaFed = eArr.length ? Math.round(eArr.reduce((s, e) => s + e.recursos.agua, 0) / eArr.length) : 0;
  const energiaFed = eArr.length ? Math.round(eArr.reduce((s, e) => s + e.recursos.energia, 0) / eArr.length) : 0;
  const presupuestoFed = state.presupuestoFederal;

  const estadoData = estadoSeleccionado ? state.estados[Object.keys(state.estados).find(k => state.estados[k].nombre === estadoSeleccionado)] : null;

  const reglasActuales = costosBeneficios[invertirRecurso];
  const esDistribucion = invertirRecurso === 'Distribución';
  const multTransporte = esDistribucion && transporteModo
    ? costosBeneficios.Distribución.modos[transporteModo]
    : null;

  function cerrarModal() {
    setModal(null);
  }

  function confirmarInversion() {
    if (!estadoSeleccionado || !estadoData) return;
    const id = Object.keys(state.estados).find(k => state.estados[k].nombre === estadoSeleccionado);
    if (!id) return;
    dispatch({
      type: 'INVERTIR_RECURSO',
      payload: {
        estado: id,
        recurso: invertirRecurso,
        transporte: esDistribucion ? transporteModo : null,
      }
    });
    setModal(null);
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
          <div className="modal-ventana modal-ventana-invertir" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <span className="modal-icon">◈</span>
              <span className="modal-titulo">INVERTIR EN {estadoSeleccionado}</span>
              <button className="modal-cerrar" onClick={cerrarModal}>✕</button>
            </div>
            <div className="modal-cuerpo">
              <div className="modal-campo">
                <label className="modal-label">CATEGORÍA DE INVERSIÓN</label>
                <select
                  className="modal-select"
                  value={invertirRecurso}
                  onChange={(e) => { setInvertirRecurso(e.target.value); setTransporteModo('Terrestre'); }}
                >
                  {opcionesRecursos.map((r) => (
                    <option key={r} value={r}>{r}</option>
                  ))}
                </select>
              </div>

              {esDistribucion && (
                <div className="modal-campo">
                  <label className="modal-label">MEDIO DE TRANSPORTE</label>
                  <select
                    className="modal-select"
                    value={transporteModo}
                    onChange={(e) => setTransporteModo(e.target.value)}
                  >
                    {Object.entries(costosBeneficios.Distribución.modos).map(([modo, data]) => (
                      <option key={modo} value={modo}>
                        {modo} ({data.costoPresupuesto}x costo / {data.beneficio}x efectividad)
                      </option>
                    ))}
                  </select>
                </div>
              )}

              <div className="inv-cb-grid">
                <div className="inv-cb-column inv-cb-consume">
                  <div className="inv-cb-header inv-cb-header-consume">
                    ⚠️ SE CONSUMIRÁ
                  </div>
                  {Object.entries(reglasActuales.consume).map(([recurso, costo]) => {
                    let finalCosto = costo;
                    if (multTransporte && recurso === 'Presupuesto') {
                      finalCosto = Math.round(costo * multTransporte.costoPresupuesto);
                    }
                    return (
                      <div key={recurso} className="inv-cb-row">
                        <span className="inv-cb-recurso">{recurso}</span>
                        <span className="inv-cb-num inv-cb-num-consume">-{finalCosto}</span>
                      </div>
                    );
                  })}
                </div>
                <div className="inv-cb-column inv-cb-genera">
                  <div className="inv-cb-header inv-cb-header-genera">
                    ✅ SE GENERARÁ
                  </div>
                  {Object.entries(reglasActuales.genera).map(([recurso, beneficio]) => {
                    let finalBene = beneficio;
                    if (multTransporte && recurso !== 'Bienestar') {
                      finalBene = Math.round(beneficio * multTransporte.beneficio);
                    }
                    return (
                      <div key={recurso} className="inv-cb-row">
                        <span className="inv-cb-recurso">{recurso}</span>
                        <span className="inv-cb-num inv-cb-num-genera">+{finalBene}</span>
                      </div>
                    );
                  })}
                </div>
              </div>

              <div className="modal-desc inv-cb-aviso">
                {estadoData && estadoData.presupuesto < (multTransporte
                  ? Math.round(reglasActuales.consume.Presupuesto * multTransporte.costoPresupuesto)
                  : reglasActuales.consume.Presupuesto) ? (
                  <span className="inv-cb-insuficiente">⚠️ PRESUPUESTO INSUFICIENTE</span>
                ) : (
                  <span>Presupuesto disponible: <strong>${estadoData ? estadoData.presupuesto : 0}M</strong></span>
                )}
              </div>

              <div className="modal-accion">
                <button
                  className="btn btn-invertir"
                  disabled={estadoData && estadoData.presupuesto < (multTransporte
                    ? Math.round(reglasActuales.consume.Presupuesto * multTransporte.costoPresupuesto)
                    : reglasActuales.consume.Presupuesto)}
                  onClick={confirmarInversion}
                >
                  ⚡ AUTORIZAR INVERSIÓN
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
                  <div className="analisis-item">
                    <span className="analisis-label">INFRAESTRUCTURA</span>
                    <div className="analisis-bar-bg">
                      <div className="analisis-bar-fill" style={{ width: `${estadoData.recursos.infraestructura}%`, background: '#88C440' }} />
                    </div>
                    <span className="analisis-valor">{Math.round(estadoData.recursos.infraestructura)}%</span>
                  </div>
                  <div className="analisis-item">
                    <span className="analisis-label">D. SOCIAL Y CULTURAL</span>
                    <div className="analisis-bar-bg">
                      <div className="analisis-bar-fill" style={{ width: `${estadoData.recursos.desarrolloSociocultural}%`, background: '#ce93d8' }} />
                    </div>
                    <span className="analisis-valor">{Math.round(estadoData.recursos.desarrolloSociocultural)}%</span>
                  </div>
                  <div className="analisis-item">
                    <span className="analisis-label">DISTRIBUCIÓN</span>
                    <div className="analisis-bar-bg">
                      <div className="analisis-bar-fill" style={{ width: `${estadoData.recursos.distribucion}%`, background: '#81d4fa' }} />
                    </div>
                    <span className="analisis-valor">{Math.round(estadoData.recursos.distribucion)}%</span>
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
