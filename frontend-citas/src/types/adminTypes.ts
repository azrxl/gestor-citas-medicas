export interface MedicoApprovalDTO {
    id: number;
    login: string;
    nombre: string;
    apellido: string;
    aprobado: boolean;
}

export interface AdminPendingResponseDTO {
    username: string;
    medicos: MedicoApprovalDTO[];
}