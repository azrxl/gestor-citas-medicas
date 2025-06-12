import axios from 'axios';
import type {ApiCita, BloqueCita, CitasActivas, Cita, CitaDTOPerfil} from '../types/types.ts';

const API_BASE_URL = 'http://localhost:8080/api';

// --- LÓGICA PARA LA PÁGINA DE INICIO (HOME) ---

// Mapa de la respuesta cruda de la API: la clave es el ID del médico (string)
type RawApiResponseHome = {
    [idMedico: string]: ApiCita[];
}

/**
 * Función transformadora para la página de inicio.
 * Sigue limitando a 3 días y ahora incluye el estado de la cita.
 */
const transformApiCitasHome = (rawData: RawApiResponseHome): CitasActivas => {
    return Object.entries(rawData).reduce((acc, [medicoId, citasMedico]) => {

        const citasAgrupadasPorFecha = citasMedico.reduce((dateAcc, cita) => {
            const { fecha } = cita;
            if (!dateAcc[fecha]) {
                dateAcc[fecha] = [];
            }

            // --- CAMBIO CLAVE: Añadimos 'estado' al objeto Cita ---
            // Además del id y la hora formateada, ahora pasamos el estado.
            const horaFormateada = cita.horaInicio.slice(0, 5);
            dateAcc[fecha].push({
                id: cita.id,
                horaInicio: horaFormateada,
                // Aseguramos que el estado coincida con nuestro tipo definido
                estado: cita.estado as Cita['estado']
            });

            return dateAcc;
        }, {} as { [date: string]: Cita[] }); // Usamos la interfaz Cita para mayor claridad

        // La lógica para limitar a 3 días se mantiene igual
        const sortedDates = Object.keys(citasAgrupadasPorFecha).sort();
        const nextThreeDays = sortedDates.slice(0, 3);
        const bloquesDeCitas: BloqueCita[] = nextThreeDays.map(date => ({
            date: date,
            citas: citasAgrupadasPorFecha[date],
        }));

        if (bloquesDeCitas.length > 0) {
            acc[Number(medicoId)] = bloquesDeCitas;
        }

        return acc;
    }, {} as CitasActivas);
};

/**
 * Obtiene las citas disponibles para la página de inicio (Home).
 */
export const getCitasDisponiblesAgrupadas = async (): Promise<CitasActivas> => {
    try {
        const response = await axios.get<RawApiResponseHome>(`${API_BASE_URL}/medicos/citas/activas`);
        return transformApiCitasHome(response.data);

    } catch (error: any) {
        throw new Error(error.response?.data?.message || 'No se pudo obtener las citas disponibles');
    }
};


// --- LÓGICA PARA LA PÁGINA DE PERFIL DE MÉDICO ---

/**
 * Función transformadora para el perfil del médico.
 * Toma un array de citas de un solo médico y lo convierte en bloques por fecha.
 * No limita el número de días.
 */
const transformHorarioMedico = (citasMedico: ApiCita[]): BloqueCita[] => {
    // 1. Agrupamos todas las citas por fecha
    const citasAgrupadasPorFecha = citasMedico.reduce((dateAcc, cita) => {
        const { fecha } = cita;
        if (!dateAcc[fecha]) {
            dateAcc[fecha] = [];
        }

        const horaFormateada = cita.horaInicio.slice(0, 5);
        dateAcc[fecha].push({
            id: cita.id,
            horaInicio: horaFormateada,
            estado: cita.estado as Cita['estado'],
        });

        return dateAcc;
    }, {} as { [date: string]: Cita[] });

    // 2. Convertimos el objeto agrupado en un array de BloqueCita y lo ordenamos por fecha
    return Object.entries(citasAgrupadasPorFecha)
        .map(([date, citas]) => ({
            date,
            citas,
        }))
        .sort((a, b) => a.date.localeCompare(b.date)); // Ordenamos los bloques cronológicamente
};

/**
 * NUEVA FUNCIÓN: Obtiene el horario completo de un médico específico para su página de perfil.
 */
export const getHorarioCompletoPorMedico = async (medicoId: string): Promise<BloqueCita[]> => {
    try {
        // La API devuelve un array de citas para el médico específico
        const response = await axios.get<ApiCita[]>(`${API_BASE_URL}/medicos/${medicoId}/citas`);

        // Pasamos los datos crudos a su propia función transformadora
        return transformHorarioMedico(response.data);
    } catch (error: any) {
        throw new Error(error.response?.data?.message || 'No se pudo obtener el horario del médico');
    }
}

// La función transformadora ahora trabaja con CitaDTO
export const transformarCitasEnBloques = (citas: CitaDTOPerfil[]): BloqueCita[] => {
    const citasPorFecha = citas.reduce((acc, cita) => {
        const { fecha } = cita;
        if (!acc[fecha]) acc[fecha] = [];
        acc[fecha].push(cita);
        return acc;
    }, {} as Record<string, CitaDTOPerfil[]>);

    return Object.entries(citasPorFecha)
        .map(([date, citasDelDia]) => ({ date, citas: citasDelDia }))
        .sort((a, b) => a.date.localeCompare(b.date));
};
