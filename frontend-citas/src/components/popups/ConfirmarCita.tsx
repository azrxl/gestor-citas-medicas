// src/components/citas/ConfirmarCita.tsx

import '../../assets/css/popup-cita.css';
import type { ConfirmCitaResponseDTO } from '../../types/citaTypes';

interface ConfirmarCitaPopupProps {
    isOpen: boolean;
    onClose: () => void;
    onConfirm: (citaId: number) => void;
    citaInfo: ConfirmCitaResponseDTO | null;
}

export const ConfirmarCita = ({ isOpen, onClose, onConfirm, citaInfo }: ConfirmarCitaPopupProps) => {
    if (!isOpen || !citaInfo) return null;

    const {cita, medico} = citaInfo;

    return (
        <div className={`wrapper ${isOpen ? 'active-popup' : ''}`} onClick={onClose}>
            <div className="popup-content" onClick={(e) => e.stopPropagation()}>
                <span className="icon-close" onClick={onClose}>X</span>
                <div className="form-box">
                    <div className="form-container">
                        <div className="appointment-details">
                            <h3>Información de la Cita</h3>
                            <div className="input-box">
                                <p><strong>Fecha:</strong> {cita.fecha}</p>
                            </div>
                            <div className="input-box">
                                <p><strong>Hora de Inicio:</strong> {cita.horaInicio}</p>
                            </div>
                            <div className="input-box">
                                <p><strong>Hora de Fin:</strong> {cita.horaFin}</p>
                            </div>
                            <div className="input-box">
                                <p><strong>Estado:</strong> {cita.estado}</p>
                            </div>
                        </div>
                        <div className="doctor-details">
                            <h3>Información del Médico</h3>
                            <div className="input-box">
                                <p><strong>Nombre:</strong> {medico.nombre} {medico.apellido}</p>
                            </div>
                            <div className="input-box">
                                <p><strong>Especialidad:</strong> {medico.especialidad}</p>
                            </div>
                            <div className="input-box">
                                <p><strong>Localidad:</strong> {medico.localidad}</p>
                            </div>
                            <div className="input-box">
                                <p><strong>Costo de Consulta:</strong> ₡{medico.costoConsulta.toLocaleString()}</p>
                            </div>
                        </div>
                    </div>
                    <div className="popup-actions">
                        <button onClick={onClose} className="btn btn-secondary">Cancelar</button>
                        {cita.estado === 'ACTIVA' && (
                            <button onClick={() => onConfirm(cita.id)} className="btn btn-primary">Confirmar</button>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}