import { useAuth } from '../../context/AuthContext';
import { Navigate, Outlet } from 'react-router-dom';

export const UserRoute = () => {
    const { user } = useAuth();

    // Si el usuario está logueado Y su rol es ADMIN...
    if (user && user.rol === 'ADMIN') {
        // ...lo redirigimos a su panel. No puede estar aquí.
        return <Navigate to="/admin/aprobaciones" replace />;
    }

    // Si es un paciente, un médico, o no está logueado, le permitimos ver la página.
    return <Outlet />;
};