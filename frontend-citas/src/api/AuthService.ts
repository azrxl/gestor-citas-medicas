import axios from 'axios';
import type { RegisterPayload, LoginCredentials, LoginResponse } from '../types/types.ts'

export const API_URL = 'http://localhost:8080/api';

/**
 * Envía la petición de registro al backend.
 * Esperamos una respuesta de texto plano.
 */
export const registerUser = async (userData: RegisterPayload): Promise<string> => {
    try {
        const response = await axios.post(
            `${API_URL}/auth/register`, // Endpoint de registro
            userData,                      // Datos del usuario (username, password)
        );
        // axios devuelve la respuesta en response.data
        // Si el backend envía texto plano, response.data será ese texto.
        return response.data;
    } catch (error: any) {
        // Si axios lanza un error (ej. por un status 4xx o 5xx),
        // el mensaje del backend suele estar en error.response.data
        throw error;
    }
};

/**
 * Envía la petición de login al backend.
 * Esperamos una respuesta de texto plano.
 */
export const loginUser = async (credentials: LoginCredentials): Promise<LoginResponse> => {
    try {
        const response = await axios.post<LoginResponse>(
            `${API_URL}/auth/login`,   // Endpoint de login
            credentials                   // Credenciales (username, password)
        );
        return response.data;
    } catch (error: any) {
        throw error;
    }
};