import apiClient from './axiosConfig';
import type { CitaDTO } from '../types/perfilTypes'; // O tu archivo de tipos

const API_BASE_URL = '/medico/citas'; // La base de tus endpoints de acciones

export const MedicoActionsService = {
    /**
     * Llama al endpoint para marcar una cita como COMPLETADA.
     * @param citaId El ID de la cita a completar.
     * @returns La cita actualizada.
     */
    completarCita: async (citaId: number): Promise<CitaDTO> => {
        try {
            // Tu endpoint es POST /api/medico/citas/completar/{id}
            const response = await apiClient.post<CitaDTO>(`${API_BASE_URL}/completar/${citaId}`);
            return response.data;
        } catch (error: any) {
            // Aquí puedes usar tu manejador de errores global
            throw new Error(error.response?.data?.message || 'No se pudo completar la cita.');
        }
    },

    /**
     * Llama al endpoint para cancelar una cita.
     * @param citaId El ID de la cita a cancelar.
     * @returns La cita actualizada.
     */
    cancelarCita: async (citaId: number): Promise<CitaDTO> => {
        try {
            // Tu endpoint es POST /api/medico/citas/cancelar/{id}
            const response = await apiClient.post<CitaDTO>(`${API_BASE_URL}/cancelar/${citaId}`);
            return response.data;
        } catch (error: any) {
            // Aquí puedes usar tu manejador de errores global
            throw new Error(error.response?.data?.message || 'No se pudo cancelar la cita.');
        }
    }
};