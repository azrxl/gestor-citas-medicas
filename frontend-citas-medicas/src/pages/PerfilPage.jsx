import { useState as useStatePP, useEffect as useEffectPP } from 'react';
import { getCitas as apiGetCitas } from '../services/api.js';

function PerfilPage({ user }) {
    const [citas, setCitas] = useStatePP([]);

    useEffectPP(() => {
        if(user) {
            apiGetCitas(user.id, user.rol).then(setCitas);
        }
    }, [user]);

    if (!user) return <p>Cargando perfil...</p>;

    return (
        <div>
            <h1>Perfil de {user.nombre}</h1>
            <div className="appointments">
                <h2>Histórico de Citas</h2>
                {citas.length > 0 ? (
                    <table>
                        <thead><tr>
                            <th>{user.rol === 'MEDICO' ? 'Paciente' : 'Doctor'}</th>
                            <th>Fecha</th><th>Hora Inicio</th><th>Estado</th><th>Acciones</th>
                        </tr></thead>
                        <tbody>
                        {citas.map(cita => (
                            <tr key={cita.id}>
                                <td>{user.rol === 'MEDICO' ? cita.loginPaciente : cita.loginMedico}</td>
                                <td>{cita.fecha}</td><td>{cita.horaInicio}</td><td>{cita.estado}</td>
                                <td>-</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : <p>No se encontró historial de citas.</p>}
            </div>
        </div>
    );
}

export default PerfilPage;
