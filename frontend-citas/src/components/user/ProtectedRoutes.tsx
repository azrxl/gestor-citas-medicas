// src/components/user/ProtectedRoute.tsx
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export const ProtectedRoute = () => {
    const { user } = useAuth();
    const location = useLocation();

    if (!user) {
        return <Navigate to="/" replace />;
    }

    // Este check evita bucles cuando YA estás en /medico/completar
    if (user.rol === 'MEDICO' && !user.profileComplete && location.pathname !== '/medico/completar') {
        return <Navigate to="/medico/completar" replace />;
    }

    return <Outlet />;
};
