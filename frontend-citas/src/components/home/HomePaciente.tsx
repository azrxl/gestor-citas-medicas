import {useEffect, useMemo, useState} from "react";
import SearchBar from "../home/SearchBar.tsx";
import ListaMedicos  from "../home/ListaMedicos.tsx";
import LoadingSpinner from "../loading/LoadingSpinner.tsx";
import { getMedicos } from "../../api/MedicosService.ts";
import { getCitasDisponiblesAgrupadas } from "../../api/CitasService.ts";
import type {Medico, CitasActivas} from "../../types/types.ts";
import {useAuth} from "../../context/AuthContext.tsx";


function HomePaciente() {
    // --- ESTADO Y DATOS ---
    const { user } = useAuth();
    const especialidades: string[] = ["Cardiología", "Dermatología", "Pediatría", "Neurología"];
    const ciudades: string[] = ["San José", "Heredia", "Alajuela", "Cartago"];
    const [filtroEspecialidad, setFiltroEspecialidad] = useState("");
    const [filtroCiudad, setFiltroCiudad] = useState("");
    const [medicos, setMedicos] = useState<Medico[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<String | null>(null);
    const [citasActivas, setCitasActivas] = useState<CitasActivas>({});

    useEffect(() => {
        const fetchData = async () => {
            try {
                setIsLoading(true);
                setError(null);

                // Promise.all permite que ambas llamadas a la API se ejecuten en paralelo
                const [medicosData, citasData] = await Promise.all([
                    getMedicos(),
                    getCitasDisponiblesAgrupadas() // Llama al servicio de citas
                ]);

                // Actualiza ambos estados cuando los datos llegan
                setMedicos(medicosData);
                setCitasActivas(citasData);

            } catch (err: any) {
                setError(err.message || "Ocurrió un error al cargar los datos.");
                setMedicos([]);
                setCitasActivas({});
            } finally {
                setIsLoading(false);
            }
        };
        fetchData();
    }, []); // El array vacío asegura que se ejecute solo una vez.

    // --- LOGICA ---

    const medicosFiltrados: Medico[] = useMemo(() => {
        if (!filtroEspecialidad && !filtroCiudad) {
            return medicos;
        }
        return medicos.filter(medico => {
          const coincideEspecialidad = !filtroEspecialidad || medico.especialidad === filtroEspecialidad;
          const coincideCiudad = !filtroCiudad || medico.localidad === filtroCiudad;
          return coincideEspecialidad && coincideCiudad;
        });
    }, [filtroEspecialidad, filtroCiudad, medicos]);

    // --- RENDER ---

    if (isLoading) return <>
        <LoadingSpinner/>
    </>

    if (error) return <div>{error}</div>;

    return (
        <>
        <h1>{ (user) ? `Sesion iniciada como ${user.rol.toLowerCase()}` : `Bienvenido`}</h1>
        <h2>Lista de Medicos Disponibles</h2>
        <SearchBar especialidades={especialidades}
                   ciudades={ciudades}
                   filtroEspecialidad={filtroEspecialidad}
                   filtroCiudad={filtroCiudad}
                   onEspecialidadChange={setFiltroEspecialidad}
                   onCiudadChange={setFiltroCiudad}
        />

        <ListaMedicos medicos={medicosFiltrados}
                      citasActivas={citasActivas}
        />
        </>
    )
}

export default HomePaciente;