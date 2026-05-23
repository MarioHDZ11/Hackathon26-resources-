import axios from 'axios';

const API_BASE = process.env.REACT_APP_API_URL || 'http://localhost:8081/api';

const client = axios.create({
  baseURL: API_BASE,
  timeout: 30000,
  headers: { 'Content-Type': 'application/json' },
});

export async function fetchEstados() {
  const { data } = await client.get('/estados');
  return data;
}

export async function fetchEstadoById(id) {
  const { data } = await client.get(`/estados/${id}`);
  return data;
}

export async function fetchEstadoByNombre(nombre) {
  const { data } = await client.get(`/estados/nombre/${encodeURIComponent(nombre)}`);
  return data;
}

export async function fetchMunicipios(estadoId) {
  const { data } = await client.get(`/estados/${estadoId}/municipios`);
  return data;
}

export async function fetchMunicipiosByNombre(nombre) {
  const { data } = await client.get(`/estados/nombre/${encodeURIComponent(nombre)}/municipios`);
  return data;
}

export async function fetchGameConfig() {
  const { data } = await client.get('/game/config');
  return data;
}

export async function fetchInitialGameState() {
  const { data } = await client.get('/game/initial');
  return data;
}

const api = {
  fetchEstados,
  fetchEstadoById,
  fetchEstadoByNombre,
  fetchMunicipios,
  fetchMunicipiosByNombre,
  fetchGameConfig,
  fetchInitialGameState,
};

export default api;
