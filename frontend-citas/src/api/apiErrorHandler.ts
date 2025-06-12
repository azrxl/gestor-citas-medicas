import axios, { AxiosError } from 'axios';
import type { ErrorResponseDTO } from '../types/types';

/**
 * Procesa un error de Axios y devuelve un mensaje de error significativo.
 * @param error El objeto de error capturado.
 * @param defaultMessage Un mensaje por defecto si no se puede encontrar uno específico.
 * @returns Un string con el mensaje de error.
 */
export const handleApiError = (error: unknown, defaultMessage: string): string => {
    if (axios.isAxiosError(error)) {
        const axiosError = error as AxiosError<ErrorResponseDTO>;
        // Si el backend envía nuestro DTO de error, usamos su mensaje
        if (axiosError.response?.data?.message) {
            return axiosError.response.data.message;
        }
    }
    // Si no, devolvemos el mensaje por defecto
    return defaultMessage;
};