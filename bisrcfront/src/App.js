import React, { useState } from 'react';
import './App.css';
import { GameStateProvider } from './context/GameStateContext';
import Header from './components/Header';
import Tablero from './components/Tablero';
import Controles from './components/Controles';

function AppInner() {
  const [estadoSeleccionado, setEstadoSeleccionado] = useState(null);

  return (
    <div className="app">
      <Header />
      <Tablero
        estadoSeleccionado={estadoSeleccionado}
        setEstadoSeleccionado={setEstadoSeleccionado}
      />
      <Controles estadoSeleccionado={estadoSeleccionado} />
    </div>
  );
}

function App() {
  return (
    <GameStateProvider>
      <AppInner />
    </GameStateProvider>
  );
}

export default App;
