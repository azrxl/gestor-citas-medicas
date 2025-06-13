export interface IntervaloDTO {
    inicio: string | null;
    fin:    string | null;
}

export interface DiaDTO {
    manana: IntervaloDTO;
    tarde:  IntervaloDTO;
}

export interface MedicoProfileDTO {
    id:             number;
    cedula:         string;
    nombre:         string;
    apellido:       string;
    login:          string;
    especialidad:   string;
    costoConsulta:  number;
    localidad:      string;
    frecuenciaCita: number;
    horario:        Record<string, DiaDTO>;
}

export interface MedicoUpdateRequestDTO {
    especialidad:    string;
    costoConsulta:   number;
    localidad:       string;
    frecuenciaCita:  number;
    horario:         Record<string, DiaDTO>;
}
