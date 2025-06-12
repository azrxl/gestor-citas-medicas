// src/api/citaService.ts

import apiClient from '../api/axiosConfig.ts';
import type {
    ConfirmCitaResponseDTO
} from '../types/citaTypes';

export const getConfirmCita = async (citaId: number): Promise<ConfirmCitaResponseDTO> => {
    const resp = await apiClient.get<ConfirmCitaResponseDTO>(`/citas/info/${citaId}`);
    return resp.data;
};

export const confirmarCita = async (citaId: number): Promise<void> => {
    await apiClient.put(`/citas/reservar/${citaId}`);
};
