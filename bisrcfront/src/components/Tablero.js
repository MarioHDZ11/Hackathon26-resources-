import React, { useState, useRef, useEffect } from 'react';
import { useGameState } from '../context/GameStateContext';
import { fetchEstados, fetchMunicipiosByNombre } from '../services/api';
import svgEstados from '../data/svgPaths';

function obtenerClaseBienestar(porcentaje) {
  if (porcentaje <= 25) return 'estado-alerta';
  if (porcentaje <= 50) return 'estado-riesgo';
  if (porcentaje <= 75) return 'estado-estable';
  return 'estado-optimo';
}

function Tablero({ estadoSeleccionado, setEstadoSeleccionado }) {
  const gameState = useGameState();
  const [hovered, setHovered] = useState(null);
  const [hoveredInfo, setHoveredInfo] = useState(null);
  const [animSpeed, setAnimSpeed] = useState(0.3);
  const [shadowIntensity, setShadowIntensity] = useState(0.7);
  const [estados] = useState(svgEstados);
  const [simbolos, setSimbolos] = useState({});
  const [municipios, setMunicipios] = useState([]);
  const [municipiosError, setMunicipiosError] = useState(null);
  const [municipiosLoading, setMunicipiosLoading] = useState(false);
  const svgRef = useRef(null);
  const hoverTimeout = useRef(null);

  useEffect(() => {
    fetchEstados().then((data) => {
      const simMap = {};
      for (const e of data) {
        simMap[e.id] = { simbolo: e.simbolo || '🏛️', descripcion: e.descripcion || '' };
      }
      setSimbolos(simMap);
    }).catch((err) => {
      console.error('Error al cargar estados desde la API:', err);
    });
  }, []);

  useEffect(() => {
    if (estadoSeleccionado) {
      setMunicipiosLoading(true);
      setMunicipiosError(null);
      fetchMunicipiosByNombre(estadoSeleccionado).then((data) => {
        setMunicipios(data);
        setMunicipiosError(null);
      }).catch((err) => {
        console.error('Error fetching municipios:', err);
        setMunicipios([]);
        setMunicipiosError('No se pudieron cargar los municipios. Verifica que el backend esté corriendo.');
      }).finally(() => setMunicipiosLoading(false));
    } else {
      setMunicipios([]);
      setMunicipiosError(null);
    }
  }, [estadoSeleccionado]);

  useEffect(() => {
    return () => {
      if (hoverTimeout.current) clearTimeout(hoverTimeout.current);
    };
  }, []);

  function handleMouseEnter(e, estado) {
    setHovered(estado.name);
    if (hoverTimeout.current) clearTimeout(hoverTimeout.current);
    if (svgRef.current) {
      const pathEl = document.getElementById(estado.id);
      if (pathEl) {
        const pathRect = pathEl.getBoundingClientRect();
        const containerRect = svgRef.current
          .closest('.mapa-contenedor')
          .getBoundingClientRect();
        const x = pathRect.left + pathRect.width / 2 - containerRect.left;
        const y = pathRect.top - containerRect.top;
        const sim = simbolos[estado.id];
        if (sim) {
          setHoveredInfo({
            name: estado.name,
            id: estado.id,
            simbolo: sim.simbolo,
            descripcion: sim.descripcion,
            x,
            y,
            w: pathRect.width,
          });
        }
      }
    }
  }

  function handleMouseLeave() {
    setHovered(null);
    hoverTimeout.current = setTimeout(() => {
      setHoveredInfo(null);
    }, 400);
  }

  return (
    <main className={`tablero ${estadoSeleccionado ? 'tablero-con-seleccion' : ''}`}>
      <div className="tablero-header">
        <div className="tablero-titulo">
          <span className="tablero-icon">◈</span>
          MAPA DE MÉXICO
        </div>
        <div className="tablero-info">
          <span className={`tablero-estado ${hovered ? 'active' : ''}`}>
            {hovered ? hovered.toUpperCase() : 'SISTEMA ACTIVO'}
          </span>
        </div>
      </div>
      <div className="tablero-cuerpo">
        <div className="tablero-col-mapa">
          <div className="mapa-contenedor" style={{ position: 'relative', overflow: 'visible' }}>
            <svg
              ref={svgRef}
              viewBox="0 0 793 498"
              className="mapa-svg"
              xmlns="http://www.w3.org/2000/svg"
              style={{
                '--anim-speed': `${animSpeed}s`,
                '--shadow-intensity': shadowIntensity,
              }}
            >
              {[...estados].sort((a, b) => {
                const aH = a.name === hovered || a.name === estadoSeleccionado ? 1 : 0;
                const bH = b.name === hovered || b.name === estadoSeleccionado ? 1 : 0;
                return aH - bH;
              }).map((e) => {
                const eData = gameState.estados[e.id];
                const isHovered = hovered === e.name;
                const isSelected = estadoSeleccionado === e.name;
                const claseBienestar = eData ? obtenerClaseBienestar(eData.bienestar) : '';
                return (
                <path
                  key={e.id}
                  id={e.id}
                  name={e.name}
                  d={e.d}
                  className={[
                    'estado-path',
                    claseBienestar,
                    isHovered ? 'estado-hovered' : '',
                    isSelected ? 'estado-selected' : '',
                  ].filter(Boolean).join(' ')}
                  onMouseEnter={(ev) => handleMouseEnter(ev, e)}
                  onMouseLeave={handleMouseLeave}
                  onClick={() => setEstadoSeleccionado(estadoSeleccionado === e.name ? null : e.name)}
                />
                );
              })}
            </svg>
            {hoveredInfo && (
              <div
                className="estado-tooltip"
                style={{
                  left: hoveredInfo.x,
                  top: hoveredInfo.y - 60,
                }}
              >
                <div className="estado-tooltip-simbolo">{hoveredInfo.simbolo}</div>
                <div className="estado-tooltip-info">
                  <div className="estado-tooltip-nombre">{hoveredInfo.name}</div>
                  <div className="estado-tooltip-desc">{hoveredInfo.descripcion}</div>
                </div>
              </div>
            )}
          </div>
          <div className="mapa-controles">
            <div className="mapa-control-item">
              <label className="mapa-control-label">
                SUAVIDAD <span>{animSpeed.toFixed(1)}s</span>
              </label>
              <input
                type="range"
                min="0.1"
                max="1.0"
                step="0.05"
                value={animSpeed}
                onChange={(e) => setAnimSpeed(parseFloat(e.target.value))}
                className="mapa-control-slider"
              />
            </div>
            <div className="mapa-control-item">
              <label className="mapa-control-label">
                SOMBRA <span>{Math.round(shadowIntensity * 100)}%</span>
              </label>
              <input
                type="range"
                min="0.1"
                max="1.0"
                step="0.05"
                value={shadowIntensity}
                onChange={(e) => setShadowIntensity(parseFloat(e.target.value))}
                className="mapa-control-slider"
              />
            </div>
          </div>
          <div className="panel-nacional">
            <div className="panel-nacional-header">
              <span className="panel-nacional-titulo">INDICADORES NACIONALES</span>
              <span className="panel-nacional-pob">
                POB: {(gameState.poblacionTotal / 1e6).toFixed(1)}M
              </span>
            </div>
            <div className="panel-nacional-cuerpo">
              <div className="panel-nacional-barras">
                {[
                  { id: 'aguaPromedio', label: 'AGUA', color: '#00b0ff' },
                  { id: 'energiaPromedio', label: 'ENERGÍA', color: '#ffea00' },
                  { id: 'alimentoPromedio', label: 'ALIMENTO', color: '#ff9100' },
                  { id: 'saludPromedio', label: 'SALUD', color: '#ff1744' },
                  { id: 'sostenibilidadPromedio', label: 'SOSTENIBILIDAD', color: '#00e676' },
                  { id: 'infraestructuraPromedio', label: 'INFRAESTRUCTURA', color: '#88C440' },
                  { id: 'desarrolloPromedio', label: 'D. SOCIAL Y CULTURAL', color: '#ce93d8' },
                  { id: 'distribucionPromedio', label: 'DISTRIBUCIÓN', color: '#81d4fa' },
                ].map((ind) => (
                  <div key={ind.id} className="ind-nac-item">
                    <span className="ind-nac-label">{ind.label}</span>
                    <div className="ind-nac-bar-bg">
                      <div
                        className="ind-nac-bar-fill"
                        style={{ width: `${gameState[ind.id] || 0}%`, backgroundColor: ind.color }}
                      />
                    </div>
                    <span className="ind-nac-valor">{gameState[ind.id] || 0}%</span>
                  </div>
                ))}
              </div>
              <div className="panel-nacional-side">
                <div className="bienestar-nacional">
                  <span className="bienestar-label">BIENESTAR NACIONAL</span>
                  <div className="bienestar-medidor">
                    <span className="bienestar-numero">{gameState.bienestarFederal}%</span>
                  </div>
                </div>
                <div className="presupuesto-nacional-info">
                  <span className="presupuesto-nacional-label">PRESUPUESTO FED</span>
                  <span className="presupuesto-nacional-valor">${gameState.presupuestoFederal}M</span>
                </div>
              </div>
            </div>
          </div>
          {!estadoSeleccionado && (
            <div className="tablero-footer">
              <div className="tablero-leyenda">
                {[
                  { clase: 'estado-alerta', label: 'ALERTA' },
                  { clase: 'estado-riesgo', label: 'EN RIESGO' },
                  { clase: 'estado-estable', label: 'ESTABLE' },
                  { clase: 'estado-optimo', label: 'ÓPTIMO' },
                ].map((l) => (
                  <div key={l.label} className="leyenda-item">
                    <span className={`leyenda-dot ${l.clase}`} />
                    <span className="leyenda-label">{l.label}</span>
                  </div>
                ))}
              </div>
            </div>
          )}
          {estadoSeleccionado && (
            <div className="tablero-selected">
              ZONA SELECCIONADA: <strong>{estadoSeleccionado}</strong>
            </div>
          )}
        </div>
        {estadoSeleccionado && (
          <aside className="panel-lateral">
            <div className="panel-header">
              <span className="panel-icon">◈</span>
              <span className="panel-titulo">{estadoSeleccionado.toUpperCase()}</span>
              <button className="panel-cerrar" onClick={() => setEstadoSeleccionado(null)}>✕</button>
            </div>
            <div className="panel-sub">
              MUNICIPIOS · {municipios.length} REGISTROS
            </div>
            {municipiosLoading && <div className="panel-mensaje">Cargando municipios...</div>}
            {municipiosError && <div className="panel-mensaje panel-error">{municipiosError}</div>}
            <div className="panel-tabla-wrap">
              <table className="tabla-municipios">
                <thead>
                  <tr>
                    <th>MUNICIPIO</th>
                    <th>BIENESTAR</th>
                    <th>RECURSO URGENTE</th>
                  </tr>
                </thead>
                <tbody>
                  {municipios.map((m, i) => (
                    <tr key={i}>
                      <td className="td-municipio">{m.nombre}</td>
                      <td className="td-bienestar">
                        <div className="bienestar-bar">
                          <div
                            className="bienestar-fill"
                            style={{ width: `${m.bienestar}%` }}
                          />
                        </div>
                        <span className="bienestar-valor">{m.bienestar}%</span>
                      </td>
                      <td className="td-recurso">{m.recurso}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </aside>
        )}
      </div>
    </main>
  );
}

export default Tablero;
