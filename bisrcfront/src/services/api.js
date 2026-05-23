const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8082/api';

async function request(endpoint, options = {}) {
  const url = `${API_URL}${endpoint}`;
  const config = {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options,
  };
  if (config.body && typeof config.body === 'object') {
    config.body = JSON.stringify(config.body);
  }
  const res = await fetch(url, config);
  if (!res.ok) {
    const error = await res.json().catch(() => ({ error: res.statusText }));
    throw new Error(error.error || `Error ${res.status}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

export const api = {
  getEstados: () => request('/estados'),
  getEstado: (id) => request(`/estados/${id}`),
  getRecursos: () => request('/recursos'),
  getRecurso: (id) => request(`/recursos/${id}`),
  getUltimaPartida: () => request('/partidas/ultima'),
  getPartida: (id) => request(`/partidas/${id}`),
  crearPartida: (data) => request('/partidas', { method: 'POST', body: data }),
  actualizarPartida: (id, data) => request(`/partidas/${id}`, { method: 'PUT', body: data }),
};

export default api;
