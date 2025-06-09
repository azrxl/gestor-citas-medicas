import { useState as useStateCP, useEffect as useEffectCP } from 'react';
import { useNavigate as useNavigateCP } from 'react-router-dom';
import { getMedicoToComplete as apiGetMedico, updateMedicoProfile as apiUpdateMedico } from '../services/api.js';

function CompletarPerfilMedicoPage({ user }) {
    const [medico, setMedico] = useStateCP(null);
    const navigate = useNavigateCP();

    useEffectCP(() => {
        apiGetMedico(user.id).then(setMedico);
    }, [user]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const profileData = Object.fromEntries(formData.entries());
        await apiUpdateMedico(user.id, profileData);
        alert('Perfil completado exitosamente');
        navigate('/perfil');
    }

    if (!medico) return <p>Cargando...</p>;

    return (
        <div className="main">
            <h1>Completa tu Perfil</h1>
            <form onSubmit={handleSubmit}>
                <label>Cédula:</label><input type="text" defaultValue={medico.cedula} readOnly />
                <label>Nombre:</label><input type="text" defaultValue={medico.nombre} readOnly />
                <label>Especialidad:</label><input type="text" name="especialidad" required />
                <label>Costo Consulta:</label><input type="number" name="costoConsulta" required />
                <label>Localidad:</label><input type="text" name="localidad" required />
                {/* Agrega más campos si es necesario */}
                <div className="wrap"><button type="submit">Guardar Cambios</button></div>
            </form>
        </div>
    );
}

export default CompletarPerfilMedicoPage;
