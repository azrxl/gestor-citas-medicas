import { useState as useStateHP, useEffect as useEffectHP } from 'react';
import { Link as LinkHP } from 'react-router-dom';
import { getPublicMedicos as apiGetPublicMedicosHP } from '../services/api.js';
import medicoAvatarHP from '../assets/images/medico1.png';

function HomePage() {
    const [medicos, setMedicos] = useStateHP([]);
    const [isLoading, setIsLoading] = useStateHP(true);

    useEffectHP(() => {
        apiGetPublicMedicosHP({}).then(data => {
            setMedicos(data);
            setIsLoading(false);
        });
    }, []);

    if (isLoading) return <p>Cargando médicos...</p>;

    return (
        <div>
            <h1>Lista de Médicos Disponibles</h1>
            <div className="search-bar">{/* Formulario de búsqueda aquí */}</div>
            <div className="appointments">
                {medicos.length > 0 ? medicos.map(medico => (
                    <div key={medico.id} className="appointment">
                        <LinkHP to={`/perfilMedico/${medico.id}`} className="medico-link">
                            <div className="row-container-home">
                                <div className="block1">
                                    <img src={medicoAvatarHP} alt="Doctor" />
                                    <h2>{medico.nombre} {medico.apellido} - ${medico.costoConsulta}</h2>
                                </div>
                                <div className="block2">
                                    <p><strong>Especialidad:</strong> {medico.especialidad}</p>
                                    <p><strong>Localidad:</strong> {medico.localidad}</p>
                                </div>
                            </div>
                        </LinkHP>
                        <div className="block3">
                            {medico.activeCitas.map(block => (
                                <div key={block.date}>
                                    <div className="date-item"><span>{block.date}</span></div>
                                    <div className="times">
                                        {block.citas.map(cita => (
                                            <LinkHP key={cita.id} to={`/agendar/${cita.id}`} className="time-item">
                                                <span>{cita.horaInicio}</span>
                                            </LinkHP>
                                        ))}
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                )) : <p>No hay médicos disponibles.</p>}
            </div>
        </div>
    );
}

export default HomePage;