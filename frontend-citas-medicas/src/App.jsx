import { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

// Importación de Componentes y Páginas
import Layout from './components/Layout.jsx';
import HomePage from './pages/HomePage.jsx';
import LoginPage from './pages/LoginPage.jsx';
import RegistroPage from './pages/RegistroPage.jsx';
import AdminPage from './pages/AdminPage.jsx';
import PerfilPage from './pages/PerfilPage.jsx';
import CompletarPerfilMedicoPage from './pages/CompletarPerfilMedicoPage.jsx';
import { login as apiLogin } from './services/api.js';

function App() {
    const [currentUser, setCurrentUser] = useState(null);

    const handleLogin = async (loginId, password) => {
        const result = await apiLogin(loginId, password);
        if (result.success) {
            setCurrentUser(result.user);
        }
        return result;
    };

    const handleLogout = () => {
        setCurrentUser(null);
        // Al usar Navigate aquí, se asegura la redirección en el renderizado
        return <Navigate to="/" replace />;
    };

    // Componente guardián para proteger rutas
    const ProtectedRoute = ({ children, role }) => {
        if (!currentUser) {
            return <Navigate to="/login" replace />;
        }
        // Redirección si el rol no es el correcto
        if (role && currentUser.rol !== role) {
            return <Navigate to="/" replace />;
        }
        // Redirección forzada si el médico no ha completado el perfil
        if (currentUser.rol === 'MEDICO' && !currentUser.profileComplete) {
            return <Navigate to="/medico/completar-perfil" replace />;
        }
        return children;
    };

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout user={currentUser} onLogout={handleLogout} />}>
                    {/* Rutas Públicas */}
                    <Route index element={<HomePage />} />
                    <Route path="login" element={!currentUser ? <LoginPage onLogin={handleLogin} /> : <Navigate to="/" />} />
                    <Route path="registro" element={!currentUser ? <RegistroPage /> : <Navigate to="/" />} />

                    {/* Rutas Protegidas */}
                    <Route path="perfil" element={<ProtectedRoute><PerfilPage user={currentUser}/></ProtectedRoute>} />
                    <Route path="admin" element={<ProtectedRoute role="ADMIN"><AdminPage user={currentUser} /></ProtectedRoute>} />
                    <Route path="medico/completar-perfil" element={<ProtectedRoute role="MEDICO"><CompletarPerfilMedicoPage user={currentUser} /></ProtectedRoute>} />

                    {/* Rutas de ejemplo para desarrollo futuro */}
                    <Route path="perfilMedico/:id" element={<div>Página de Perfil Público de Médico</div>} />
                    <Route path="agendar/:id" element={<div>Página para Agendar Cita</div>} />

                    {/* Ruta para contenido no encontrado */}
                    <Route path="*" element={<h2>404: Página No Encontrada</h2>} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;