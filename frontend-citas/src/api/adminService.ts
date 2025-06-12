import apiClient from '../api/axiosConfig';
import type {AdminPendingResponseDTO } from '../types/adminTypes';

export const adminService = {
    /**
     * Obtiene TODOS los médicos (aprobados y pendientes).
     * El componente se encargará de filtrarlos.
     */
    getAllMedicos: async (): Promise<AdminPendingResponseDTO> => {
        try {
            const response = await apiClient.get<AdminPendingResponseDTO>('/admin/medicos');
            return response.data;
        } catch (error: any) {
            throw new Error("No se pudo obtener la lista de médicos.");
        }
    },

    /**
     * Envía la petición para aprobar un médico. Se mantiene igual.
     */
    aprobarMedico: async (medicoId: number): Promise<void> => {
        try {
            await apiClient.post(`/admin/medicos/aprobar/${medicoId}`);
        } catch (error: any) {
            throw new Error("No se pudo aprobar al médico.");
        }
    }
};