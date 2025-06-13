// src/api/medicoService.ts

import apiClient from '../api/axiosConfig';
import type {
    MedicoProfileDTO,
    MedicoUpdateRequestDTO
} from '../types/medicoTypes';

// src/api/medicoService.ts
export const getMedicoProfile = async (): Promise<MedicoProfileDTO> => {
    console.log('Llamando GET /medico/me/completar');      // <— log antes
    const resp = await apiClient.get<MedicoProfileDTO>('/medico/me/completar');
    console.log('Respuesta perfil:', resp.data);     // <— log después
    return resp.data;
};


export const updateMedicoProfile = async (
    payload: MedicoUpdateRequestDTO
): Promise<MedicoProfileDTO> => {
    const resp = await apiClient.put<MedicoProfileDTO>(
        '/medico/me/completar',
        payload
    );
    return resp.data;
};
