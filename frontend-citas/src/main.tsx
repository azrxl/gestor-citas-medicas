// src/main.tsx

import React from 'react';
import ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext.tsx';

// --- Para no duplicar, importa todos los CSS globales aquí ---
import './assets/css/header.css';
import './assets/css/footer.css';
import './assets/css/home.css';
import './assets/css/loading.css';
import './assets/css/profile.css';
import './index.css'; // Si tienes estilos base

// --- Importa tus componentes ---
import AppLayout from './AppLayout.tsx'; // El "marco"
import HomePaciente from './components/home/HomePaciente.tsx'; // Una "página"
import PerfilMedico from './pages/PerfilMedico.tsx';
import {PerfilPage} from "./pages/PerfilPage.tsx";
import {AdminRoute} from "./components/admin/AdminApproval.tsx";
import {AdminApprovalPanel} from "./components/admin/AdminApprovalPanel.tsx";
import {UserRoute} from "./components/user/UserRoute.tsx";

const router = createBrowserRouter([
    {
        path: '/',
        element: <AppLayout/>, // 1. Renderiza el marco para TODAS las rutas hijas
        children: [
            {
                element: <UserRoute />, // <-- Guardia que expulsa a los admins
                children: [
                    {
                        index: true, // Página de inicio
                        element: <HomePaciente />,
                    },
                    {
                        path: 'perfil-medico/:medicoId', // Perfil público de un médico
                        element: <PerfilMedico />,
                    },
                    {
                        path: 'perfil', // Perfil propio del usuario logueado
                        element: <PerfilPage />,
                    }
                ]
            },
            // --- GRUPO DE RUTAS EXCLUSIVAS PARA ADMINISTRADORES ---
            {
                element: <AdminRoute />, // <-- Guardia que solo deja pasar a los admins
                children: [
                    {
                        path: 'admin/aprobaciones',
                        element: <AdminApprovalPanel />
                    }
                    // Aquí podrías añadir más rutas de admin en el futuro
                ]
            }
        ]
    }
]);


const root = ReactDOM.createRoot(document.getElementById('root')!);
root.render(
    <React.StrictMode>
        <AuthProvider>
            <RouterProvider router={router} />
        </AuthProvider>
    </React.StrictMode>
);