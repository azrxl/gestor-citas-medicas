import type {CitaConfirmDTO} from "./citaTypes.ts";

export interface ApiCita {
    id: number;
    loginMedico: string;
    loginPaciente: string | null;
    fecha: string; // ej: "2024-04-12"
    horaInicio: string; // ej: "16:00:00"
    horaFin: string;
    estado: 'ACTIVA' | 'PENDIENTE' | 'CANCELADA' | 'COMPLETADA';
}

export interface Cita {
    id: number;
    horaInicio: string;
    estado: 'ACTIVA' | 'PENDIENTE' | 'CANCELADA' | 'COMPLETADA';
}

export interface BloqueCita {
    date: string;
    citas: CitaConfirmDTO[];
}

export interface Medico {
    id: number;
    nombre: string;
    apellido: string;
    costoConsulta: number;
    especialidad: string;
    localidad: string;
}

// Un map con la key asociada al id de medico junto con su bloque de citas
export type CitasActivas = {
    [idMedico: number]: BloqueCita[];
}

// Definimos los tipos para los datos que enviaremos
export interface LoginCredentials {
    login: string;
    password: string;
}

export interface LoginResponse {
    username: string;
    token: string;
    rol: "PACIENTE" | "MEDICO" | "ADMIN";
}

export interface RegisterPayload {
    cedula: string;
    nombre: string;
    apellido: string;
    login: string;
    password: string;
    rol: 'PACIENTE' | 'MEDICO'; // Usamos un tipo literal para los roles permitidos
}

export interface ErrorResponseDTO {
    timestamp: string;
    status: number;
    error: string;
    message: string;
    path: string;
}

export interface Message {
    text: string;
    type: 'success' | 'error' | 'info';
}

//para el AuthContext
export interface User {
    username: string;
    token: string;
    rol: 'PACIENTE' | 'MEDICO' | "ADMIN"; // <-- AÑADIR ESTA LÍNEA
}

export interface AuthContextType {
    user: User | null;
    login: (userData: User) => void;
    logout: () => void;
}

// src/types/types.ts

// Coincide con UsuarioDTO.java
export interface UsuarioDTOPerfil {
    cedula: string;
    nombre: string;
    apellido: string;
    login: string;
    aprobado: boolean;
    rol: 'PACIENTE' | 'MEDICO';
    // Campos opcionales que pueden venir en el perfil
    especialidad?: string;
    localidad?: string;
    costoConsulta?: number;
}

// Coincide con CitaDTO.java
export interface CitaDTOPerfil {
    id: number;
    fecha: string; // "YYYY-MM-DD"
    horaInicio: string; // "HH:mm:ss"
    horaFin: string;
    estado: 'PENDIENTE' | 'COMPLETADA' | 'CANCELADA' | 'ACTIVA';
    medicoLogin: string;
    medicoNombreCompleto: string;
    pacienteLogin?: string;
    pacienteNombreCompleto?: string;
}

// BloqueCita ahora usa CitaDTO
export interface BloqueCitaPerfil {
    date: string;
    citas: CitaDTOPerfil[];
}

// ... otras interfaces