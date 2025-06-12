// src/components/admin/AdminApprovalPanel.tsx

import {useEffect, useMemo, useState} from 'react';
import { adminService } from '../../api/adminService';
import type { MedicoApprovalDTO } from '../../types/adminTypes';
import '../../assets/css/home.css'
import LoadingSpinner from "../loading/LoadingSpinner.tsx";

export const AdminApprovalPanel = () => {
    // 1. Un único estado como "fuente de la verdad" para todos los médicos.
    const [todosLosMedicos, setTodosLosMedicos] = useState<MedicoApprovalDTO[]>([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Carga inicial de datos
    useEffect(() => {
        adminService.getAllMedicos()
            .then(data => {
                setTodosLosMedicos(data.medicos);
            })
            .catch(err => setError(err.message))
            .finally(() => setLoading(false));
    }, []);

    // 2. Usamos useMemo para crear las listas derivadas.
    //    Estas listas se recalculan automáticamente solo si 'todosLosMedicos' cambia.
    const medicosPendientes = useMemo(
        () => todosLosMedicos.filter(m => !m.aprobado),
        [todosLosMedicos]
    );

    const medicosAprobados = useMemo(
        () => todosLosMedicos.filter(m => m.aprobado),
        [todosLosMedicos]
    );

    // 3. El manejador ahora actualiza el estado local para una UI optimista.
    const handleAprobar = async (id: number) => {
        try {
            await adminService.aprobarMedico(id);

            setTodosLosMedicos(currentMedicos =>
                currentMedicos.map(m =>
                    m.id === id ? { ...m, aprobado: true } : m
                )
            );
        } catch (err: any) {
            alert(`Error aprobando: ${err.message}`);
        }
    };

    if (loading) return <LoadingSpinner />;
    if (error)   return <p className="error-message">{error}</p>;

    return <>
        <h1>Panel de Administración</h1>
        <div className="admin-panel">
            <section className="medico-list">
                <h2>Médicos en espera de aprobación</h2>
                <div className="appointments">
                    {medicosPendientes.length === 0
                        ? <p>No hay médicos pendientes.</p>
                        // Ahora renderizamos desde la lista derivada 'medicosPendientes'
                        : medicosPendientes.map(m => (
                            <div key={m.id} className="appointment">
                                <div className="row-container-home">
                                    <img src="../../../public/img/medical-logo.jpg" alt="Doctor"/>
                                    <h2>{m.nombre} {m.apellido} – User: {m.login}</h2>
                                </div>
                                <button
                                    onClick={() => handleAprobar(m.id)}
                                    className="btn-primary"
                                >
                                    Aprobar
                                </button>
                            </div>
                        ))
                    }
                </div>
            </section>

            {/* --- LISTA DE MÉDICOS APROBADOS --- */}
            <section className="medico-list">
                <h2>Médicos Aprobados</h2>
                <div className="appointments">
                    {medicosAprobados.length === 0
                        ? <p>No hay médicos aprobados.</p>
                        // Ahora renderizamos desde la lista derivada 'medicosAprobados'
                        : medicosAprobados.map(m => (
                            <div key={m.id} className="appointment approved">
                                <div className="row-container-home">
                                    <img src="../../../public/img/medical-logo.jpg" alt="Doctor"/>
                                    <h2>{m.nombre} {m.apellido} – User: {m.login}</h2>
                                </div>
                                <span className="status-approved">Aprobado</span>
                            </div>
                        ))
                    }
                </div>
            </section>
        </div>
    </>

};