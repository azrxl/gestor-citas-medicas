// src/pages/EditProfilePage.tsx
import { useState, useEffect, type FormEvent } from 'react';
import {
    getMedicoProfile,
    updateMedicoProfile
} from '../api/medicoService.ts';
import type {
    MedicoProfileDTO,
    MedicoUpdateRequestDTO,
    DiaDTO
} from '../types/medicoTypes.ts';
import '../assets/css/edit.css'

const diasSemana = ['Lunes','Martes','Miércoles','Jueves','Viernes','Sábado','Domingo'];

const createInitialHorario = (): Record<string, DiaDTO> => {
    return Object.fromEntries(
        diasSemana.map(d => [d, {
            manana: { inicio: '', fin: '' },
            tarde:  { inicio: '', fin: '' }
        }])
    );
};

export const EditProfilePage = () => {
    const [perfil, setPerfil] = useState<MedicoProfileDTO | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError]     = useState<string | null>(null);

    // Campos editables
    const [especialidad, setEspecialidad] = useState('');
    const [costo, setCosto]               = useState<number>(0);
    const [localidad, setLocalidad]       = useState('');
    const [frecuencia, setFrecuencia]     = useState<number>(0);
    // Horario: Record<string, DiaDTO>
    // top of component, before useEffect:
    const [horario, setHorario] = useState<Record<string, DiaDTO>>({})

    useEffect(() => {
        getMedicoProfile()
            .then(dto => {
                setPerfil(dto);
                setEspecialidad(dto.especialidad || '');
                setCosto(dto.costoConsulta || 0);
                setLocalidad(dto.localidad || '');
                setFrecuencia(dto.frecuenciaCita || 0);

                // MEJORA 3: Fusionar el horario del backend con una estructura completa.
                // Esto asegura que el estado 'horario' siempre tenga todos los días.
                const horarioBase = createInitialHorario();
                const horarioRecibido = dto.horario || {};
                // Sobrescribimos los días del horario base con los que vienen de la API
                for (const dia in horarioRecibido) {
                    if (horarioBase[dia]) {
                        horarioBase[dia] = horarioRecibido[dia];
                    }
                }
                setHorario(horarioBase);
            })
            .catch(e => setError(e.message))
            .finally(() => setLoading(false));
    }, []);

    // CORRECCIÓN 2: Hacer la actualización de estado anidado de forma segura.
    const handleTimeChange = (
        dia: string,
        turno: 'manana' | 'tarde',
        campo: 'inicio' | 'fin',
        valor: string
    ) => {
        setHorario(prev => ({
            ...prev,
            [dia]: {
                // Si prev[dia] no existe, usa un objeto vacío como base
                ...(prev[dia] || { manana: { inicio: '', fin: '' }, tarde: { inicio: '', fin: '' } }),
                [turno]: {
                    // Si prev[dia]?.[turno] no existe, usa un objeto vacío
                    ...(prev[dia]?.[turno] || { inicio: '', fin: '' }),
                    [campo]: valor
                }
            }
        }));
    };

    const onSubmit = async (e: FormEvent) => {
        e.preventDefault();
        if (!perfil) return;

        // --- VALIDACIÓN INTELIGENTE ANTES DE ENVIAR ---
        for (const dia in horario) {
            const turno = horario[dia];

            const mananaIncompleta = (turno.manana.inicio && !turno.manana.fin) ||
                (!turno.manana.inicio && turno.manana.fin);

            const tardeIncompleta  = (turno.tarde.inicio && !turno.tarde.fin) ||
                (!turno.tarde.inicio && turno.tarde.fin);

            if (mananaIncompleta || tardeIncompleta) {
                alert(`Por favor, complete ambos campos de la mañana o la tarde para ${dia}.`);
                return;
            }
        }

        const payload: MedicoUpdateRequestDTO = {
            especialidad,
            costoConsulta: costo,
            localidad,
            frecuenciaCita: frecuencia,
            horario
        };

        try {
            const updated = await updateMedicoProfile(payload);
            alert('Perfil actualizado correctamente.');
            setPerfil(updated);
        } catch (err: any) {
            alert(`Error al actualizar: ${err.message}`);
        }
    };

    const handleNumberChange = (setter: (value: number) => void, value: string) => {
        const num = parseInt(value, 10);
        setter(isNaN(num) ? 0 : num); // Si no es un número, lo dejamos en 0
    };

    if (loading) return <p>Cargando perfil…</p>;
    if (error)   return <p className="text-red-500">{error}</p>;
    if (!perfil) return null;

    return <>
        <div className={"main"}>
            <h1>Completa tu Perfil</h1>
            <form onSubmit={onSubmit}>
                <div className="login-info">
                    <label>Especialidad:</label>
                    <input
                        value={especialidad}
                        onChange={e => setEspecialidad(e.target.value)}
                    />
                    {/* ... resto de campos como costo, localidad, etc. ... */}
                </div>
                <div className="login-info">
                    <label>Localidad:</label>
                    <input
                        value={localidad}
                        onChange={e => setLocalidad(e.target.value)}
                    />
                </div>
                <div className="login-info">
                    <label>Costo Consulta:</label>
                    <input
                        value={costo}
                        onChange={e => handleNumberChange(setCosto, e.target.value)}
                    />
                </div>
                <div className="login-info">
                    <label>Frecuencia:</label>
                    <input
                        value={frecuencia}
                        onChange={e => handleNumberChange(setFrecuencia, e.target.value)}
                    />
                </div>

                <div className="horario-container">
                    <label>Horario de Atención:</label>
                    <div className="horarios-grid">
                        {diasSemana.map(dia => {
                            const d = horario[dia];
                            return (
                                <div key={dia} className="dia-horario">
                                    <label>{dia}:</label>
                                    <div className="bloque-horario">
                                        <label>Turno Mañana:</label>
                                        <input
                                            type="time"
                                            value={d.manana.inicio || ''}
                                            onChange={e => handleTimeChange(dia, 'manana', 'inicio', e.target.value)}
                                        />
                                        <input
                                            type="time"
                                            value={d.manana.fin || ''}
                                            onChange={e => handleTimeChange(dia, 'manana', 'fin', e.target.value)}
                                        />
                                    </div>

                                    <div className="bloque-horario">
                                        <label>Turno Tarde:</label>
                                        <input
                                            type="time"
                                            value={d.tarde.inicio || ''}
                                            onChange={e =>
                                                handleTimeChange(dia, 'tarde', 'inicio', e.target.value)
                                            }
                                        />
                                        <input
                                            type="time"
                                            value={d.tarde.fin || ''}
                                            onChange={e =>
                                                handleTimeChange(dia, 'tarde', 'fin', e.target.value)
                                            }
                                        />
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                </div>

                <div className="wrap">
                    <button type="submit">Guardar Cambios</button>
                </div>
            </form>
        </div>
    </>
};
