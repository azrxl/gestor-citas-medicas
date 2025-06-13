// src/pages/PerfilMedicoPage.tsx

import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';

// Importa los servicios que vamos a utilizar
import { getMedicoById } from '../api/MedicosService.ts';
import { getHorarioCompletoPorMedico } from '../api/CitasService.ts';

// Importa los tipos y el spinner de carga
import type { Medico, BloqueCita } from '../types/types.ts';
import LoadingSpinner from '../components/loading/LoadingSpinner.tsx';

export const PerfilMedicoPage = () => {
    // 1. OBTENER EL ID DE LA URL
    // useParams nos da el parámetro :medicoId que definimos en el router
    const { medicoId } = useParams<{ medicoId: string }>();

    // 2. DEFINIR LOS ESTADOS DEL COMPONENTE
    // Un estado para cada pieza de datos que necesitamos, más carga y error.
    const [medico, setMedico] = useState<Medico | null>(null);
    const [citas, setCitas] = useState<BloqueCita[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    // 3. OBTENER LOS DATOS DESDE LA API
    // useEffect se ejecuta cuando el componente se monta o si medicoId cambia.
    useEffect(() => {
        // Si no hay medicoId por alguna razón, no hacemos nada.
        if (!medicoId) {
            setLoading(false);
            setError("No se especificó un ID de médico.");
            return;
        }

        const fetchData = async () => {
            try {
                setLoading(true);
                setError(null);

                // Usamos Promise.all para que ambas llamadas se ejecuten en paralelo,
                // mejorando el tiempo de carga.
                const [medicoData, citasData] = await Promise.all([
                    getMedicoById(medicoId),
                    getHorarioCompletoPorMedico(medicoId)
                ]);

                // Actualizamos nuestros estados con los datos recibidos.
                setMedico(medicoData);
                setCitas(citasData);

            } catch (err: any) {
                setError(err.message || "Ocurrió un error al cargar el perfil.");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [medicoId]); // El array de dependencias asegura que esto se ejecute si cambia el ID en la URL

    // 4. RENDERIZADO CONDICIONAL
    // Mostramos diferentes cosas dependiendo del estado de la carga de datos.
    if (loading) {
        return <LoadingSpinner />;
    }

    if (error) {
        return <div className="error-message">{error}</div>;
    }

    // Es buena práctica asegurarse de que el médico existe antes de intentar renderizarlo.
    if (!medico) {
        return <div>Médico no encontrado.</div>;
    }

    // 5. RENDERIZADO FINAL (cuando todo ha cargado correctamente)
    // Este JSX no contiene <Header>, <Footer>, ni <main>, ya que se renderiza
    // dentro del <Outlet /> de AppLayout.
    return (
        <>
            <h1>Perfil de <span>{medico.nombre} {medico.apellido}</span></h1>

            <div className="profile">
                <div className="profile-header">
                    <img src="../../public/img/medical-logo.jpg" alt={`Foto de ${medico.nombre}`} />
                    <h2>{medico.nombre} {medico.apellido}</h2>
                    <p><strong>Especialidad:</strong> <span>{medico.especialidad}</span></p>
                    <p><strong>Localidad:</strong> <span>{medico.localidad}</span></p>
                    <p><strong>Costo de Consulta:</strong> <span>{medico.costoConsulta}</span></p>
                </div>
            </div>

            <div className="appointments">
                <h2>Todas las Citas Programadas</h2>

                {citas.length === 0 ? (
                    <p>No se encontraron citas para este médico.</p>
                ) : (
                    citas.map((block) => (
                        <div key={block.date}>
                            <div className="date-item">
                                <h3>{block.date}</h3>
                            </div>
                            <div className="times">
                                {block.citas.map((cita) => (
                                    <div key={cita.id} className={`time-item ${cita.estado !== 'ACTIVA' ? 'disabled' : ''}`}>
                                        {cita.estado === 'ACTIVA' ? (
                                            <Link to={`/cita/agendar/${cita.id}`}>
                                                <span>{cita.horaInicio}</span>
                                            </Link>
                                        ) : (
                                            <span>{cita.horaInicio}</span>
                                        )}
                                    </div>
                                ))}
                            </div>
                        </div>
                    ))
                )}
            </div>
        </>
    );
};