// src/types/citaTypes.ts

export interface CitaConfirmDTO {
    id: number;
    fecha: string;       // "2025-06-12"
    horaInicio: string;  // "13:00:00"
    horaFin: string;     // "13:20:00"
    estado: string;      // "ACTIVA" | "PENDIENTE" | etc.
}

export interface MedicoInfoDTO {
    id: number;
    nombre: string;
    apellido: string;
    especialidad: string;
    localidad: string;
    costoConsulta: number;
}

export interface ConfirmCitaResponseDTO {
    cita: CitaConfirmDTO;
    medico: MedicoInfoDTO;
}
