import { useAuth } from '../../context/AuthContext';
import { Navigate, Outlet } from 'react-router-dom';

export const AdminRoute = () => {
    const { user } = useAuth();

    // Si el usuario está logueado y su rol es ADMIN, le permitimos el paso.
    // <Outlet /> renderizará el componente hijo de la ruta (nuestro AdminApprovalPanel).
    if (!user || user.rol !== 'ADMIN') {
        return <Navigate to="/" replace />;
    }

    // Si es ADMIN, le permitimos ver el contenido de la ruta (el panel).
    return <Outlet />;
};