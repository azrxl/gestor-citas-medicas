interface SearchBarProps {
    especialidades: string[];
    ciudades: string[];
    filtroEspecialidad: string;
    filtroCiudad: string;
    onEspecialidadChange: (value: string) => void;
    onCiudadChange: (value: string) => void;
}

function SearchBar({
                       especialidades,
                       ciudades,
                       filtroEspecialidad,
                       filtroCiudad,
                       onEspecialidadChange,
                       onCiudadChange,
                   }: SearchBarProps) {
    return (
        <div className="search-bar">
            <div className="selector">
                <p>Especialidad:</p>
                <select id="especialidad" value={filtroEspecialidad} onChange={(e) => onEspecialidadChange(e.target.value)}>
                    <option value="">--Seleccione Especialidad--</option>
                    {especialidades.map(especialidad => (
                        <option key={especialidad} value={especialidad}>
                            {especialidad}
                        </option>
                    ))}
                </select>
            </div>
            <div className="selector">
                <p>Ciudad:</p>
                <select id="ciudad" value={filtroCiudad} onChange={(e) => onCiudadChange(e.target.value)}>
                    <option value="">--Seleccione Ciudad--</option>
                    {ciudades.map(ciudad => (
                        <option key={ciudad} value={ciudad}>
                            {ciudad}
                        </option>
                    ))}
                </select>
            </div>
        </div>
    );
}

export default SearchBar;