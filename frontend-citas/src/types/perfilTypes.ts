// src/types.ts

export interface UsuarioProfileDTO {
    id: number;
    login: string;
    nombre: string;
    apellido: string;
    rol: 'MEDICO' | 'PACIENTE';
    cedula?: string;
    especialidad?: string;
    localidad?: string;
    costoConsulta?: number;
}

export interface CitaDTO {
    id: number;
    loginMedico: string;
    loginPaciente: string;
    fecha: string;        // p.e. "2025-06-09"
    horaInicio: string;   // p.e. "13:00:00"
    horaFin: string;      // p.e. "13:20:00"
    estado: string;       // "PENDIENTE" | "COMPLETADA" | "CANCELADA"
}

export interface AppointmentBlockDTO {
    date: string;        // "2025-06-09"
    citas: CitaDTO[];
}

export interface PerfilResponseDTO {
    usuario: UsuarioProfileDTO;
    historicoCitas: AppointmentBlockDTO[];
}
