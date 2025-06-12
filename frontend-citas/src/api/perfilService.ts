// src/services/perfilService.ts

import apiClient from './axiosConfig';
import type { PerfilResponseDTO } from '../types/perfilTypes.ts';

export const getPerfil = async (): Promise<PerfilResponseDTO> => {
    try {
        const resp = await apiClient.get<PerfilResponseDTO>(`me/perfil`);
        return resp.data;
    } catch (error: any) {
        throw new Error(error.message);
    }
};
