import {API_URL} from './AuthService.ts'
import axios from "axios";
import type {Medico} from "../types/types.ts";

export const getMedicos = async (): Promise<Medico[]> => {
    try {
        const response = await axios.get(`${API_URL}/medicos`);
        return response.data;
    } catch (error: any) {
        throw new Error(error.response?.data?.message || 'No se pudo obtener la lista de médicos');
    }
};

export const getMedicoById = async (id: string): Promise<Medico> => {
    try {
        const response = await axios.get(`${API_URL}/medicos/${id}`);
        return response.data;
    } catch (error: any) {
        throw new Error(error.response?.data?.message || 'No se pudo obtener la información del médico');
    }
};


