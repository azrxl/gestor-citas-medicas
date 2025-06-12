// src/pages/PerfilPage.tsx
import '../assets/css/profile.css'
import '../assets/css/header.css'
import { useState, useEffect, useMemo } from 'react';
import { useAuth } from '../context/AuthContext.tsx';
import { getPerfil } from '../api/perfilService.ts';
import type {
    PerfilResponseDTO,
    AppointmentBlockDTO
} from '../types/perfilTypes.ts';
import LoadingSpinner from "../components/loading/LoadingSpinner.tsx";
import { MedicoActionsService } from "../api/medicoActionsService.ts";

export const PerfilPage = () => {
    const { user } = useAuth();
    const [data, setData] = useState<PerfilResponseDTO | null>(null);
    const [estado, setEstado] = useState<string>('');
    const [paciente, setPaciente] = useState<string>('');
    const [doctor, setDoctor] = useState<string>('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // 1) Fetch inicial del perfil y su histórico de citas
    const fetchPerfil = async () => {
        setLoading(true);
        try {
            const dto = await getPerfil();
            setData(dto);
        } catch (err: any) {
            setError(err.message || "Error al cargar perfil");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (user) fetchPerfil();
        else {
            setLoading(false);
            setError("Por favor, inicie sesión para ver su perfil.");
        }
    }, [user]);

    // 2) Filtrado en caliente con useMemo
    const historicoFiltrado = useMemo<AppointmentBlockDTO[]>(() => {
        if (!data) return [];

        return data.historicoCitas
            .map(block => {
                // Filtrar citas dentro de cada bloque según estado/paciente/doctor
                const citas = block.citas.filter(c => {
                    const matchEstado = estado === '' || c.estado === estado;
                    const matchPaciente = paciente === '' || c.loginPaciente.toLowerCase().includes(paciente.toLowerCase());
                    const matchDoctor  = doctor  === '' || c.loginMedico.toLowerCase().includes(doctor.toLowerCase());
                    return matchEstado && (data.usuario.rol === 'MEDICO' ? matchPaciente : matchDoctor);
                });
                return { ...block, citas };
            })
            // Eliminar bloques sin citas tras el filtrado
            .filter(block => block.citas.length > 0);
    }, [estado, paciente, doctor, data]);

    // 3) Acciones de completar / cancelar cita
    const handleAction = async (action: 'completar' | 'cancelar', citaId: number) => {
        try {
            const updated = action === 'completar'
                ? await MedicoActionsService.completarCita(citaId)
                : await MedicoActionsService.cancelarCita(citaId);

            // Actualizar sólo el bloque y cita correspondiente en data
            setData(d => {
                if (!d) return d;
                return {
                    ...d,
                    historicoCitas: d.historicoCitas.map(block => ({
                        ...block,
                        citas: block.citas.map(c => c.id === citaId ? updated : c)
                    }))
                };
            });
            alert(`Cita ${action === 'completar' ? 'completada' : 'cancelada'} con éxito.`);
        } catch (err: any) {
            console.error(err);
            alert(err.message);
        }
    };

    // 4) Render
    if (loading) return <LoadingSpinner />;
    if (error)   return <p className="text-red-500">{error}</p>;
    if (!data)   return <p>No se encontraron datos para este perfil.</p>;

    const { usuario } = data;
    const esMedico = usuario.rol === 'MEDICO';

    return (
        <div>
            <h1>Perfil de {usuario.nombre} {usuario.apellido}</h1>
            <div className="profile">
                <img
                    src={'../../public/img/medical-logo.jpg'}
                    alt="Avatar"
                />
                <div className="profile-header">
                    <h2>{usuario.nombre} {usuario.apellido}</h2>
                    {esMedico && (
                        /* Lista de detalles con su clase semántica */
                        <ul className="profile-details">
                            <li><strong>Cédula:</strong> {usuario.cedula}</li>
                            <li><strong>Especialidad:</strong> {usuario.especialidad}</li>
                            <li><strong>Localidad:</strong> {usuario.localidad}</li>
                            <li><strong>Costo:</strong> ₡{usuario.costoConsulta}</li>
                        </ul>
                    )}
                </div>
            </div>
            <div className="search-bar">
                    {/* estado */}
                    <select value={estado} onChange={e => setEstado(e.target.value)}>
                        <option value="">-- Todos --</option>
                        <option value="PENDIENTE">PENDIENTE</option>
                        <option value="CANCELADA">CANCELADA</option>
                        <option value="COMPLETADA">COMPLETADA</option>
                    </select>
                    {/* paciente/doctor */}
                    {esMedico ? (
                        <input
                            type="text"
                            placeholder="Nombre paciente"
                            value={paciente}
                            onChange={e => setPaciente(e.target.value)}
                        />
                    ) : (
                        <input
                            type="text"
                            placeholder="Nombre doctor"
                            value={doctor}
                            onChange={e => setDoctor(e.target.value)}
                        />
                    )}
            </div>

            <div className="appointments">
                <h2>Histórico de Citas</h2>
                {historicoFiltrado.length === 0
                    ? <p>No se encontró historial de citas.</p>
                    : historicoFiltrado.map(block => (
                        <div key={block.date}>
                            <h3>{block.date}</h3>
                            <table className="citas-table">
                                <thead>
                                <tr>
                                    {esMedico ? <th>Paciente</th> : <th>Doctor</th>}
                                    <th>Hora Inicio</th><th>Hora Fin</th><th>Estado</th><th>Acciones</th>
                                </tr>
                                </thead>
                                <tbody>
                                {block.citas.map(c => (
                                    <tr key={c.id}>
                                        <td>{esMedico ? c.loginPaciente : c.loginMedico}</td>
                                        <td>{c.horaInicio}</td>
                                        <td>{c.horaFin}</td>
                                        <td>{c.estado}</td>
                                        <td>
                                            {esMedico && c.estado === 'PENDIENTE' ? (
                                                <>
                                                    <button onClick={() => handleAction('completar', c.id)}>Atender</button>
                                                    <button onClick={() => handleAction('cancelar', c.id)}>Cancelar</button>
                                                </>
                                            ) : <span>-</span>}
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    ))
                }
            </div>
        </div>
    );
};
