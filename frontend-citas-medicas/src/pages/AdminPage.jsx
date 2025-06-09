import { useState as useStateAP, useEffect as useEffectAP } from 'react';
import { getMedicosPendientes as apiGetMedicosPendientes, aprobarMedico as apiAprobarMedico } from '../services/api.js';
import medicoPendienteAvatarAP from '../assets/images/medico_avatar.png';

function AdminPage({ user }) {
    const [pendientes, setPendientes] = useStateAP([]);

    useEffectAP(() => {
        apiGetMedicosPendientes().then(setPendientes);
    }, []);

    const handleAprobar = async (medicoId) => {
        await apiAprobarMedico(medicoId);
        setPendientes(pendientes.filter(p => p.id !== medicoId));
        alert('Médico aprobado');
    };

    return (
        <div>
            <h1>Bienvenido, {user.nombre}</h1>
            <h2>Médicos en espera de aprobación</h2>
            <div className="appointments">
                {pendientes.length > 0 ? pendientes.map(medico => (
                    <div key={medico.id} className="appointment">
                        <img src={medicoPendienteAvatarAP} alt="Médico pendiente" />
                        <h2 >{medico.nombre} {medico.apellido} - User: {medico.login}</h2>
                        <button onClick={() => handleAprobar(medico.id)}>Aprobar</button>
                    </div>
                )) : <p>No hay médicos pendientes de aprobación.</p>}
            </div>
        </div>
    );
}

export default AdminPage;