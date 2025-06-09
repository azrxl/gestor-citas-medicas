import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';

// Estilos globales. Asegúrate que las rutas son correctas.
import './index.css';
import './assets/css/base.css';
import './assets/css/fragment.css';
import './assets/css/login.css';
import './assets/css/profile.css';
import './assets/css/style.css';

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);