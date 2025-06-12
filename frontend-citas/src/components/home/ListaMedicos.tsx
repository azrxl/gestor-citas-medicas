import type { Medico, CitasActivas } from '../../types/types.ts';
import MedicoCard from './MedicoCard.tsx';

interface ListaMedicosProps {
    medicos: Medico[];
    citasActivas: CitasActivas;
}

function ListaMedicos({ medicos, citasActivas }: ListaMedicosProps) {
    return (
        <div className="appointments">
            {medicos.length > 0 ? (
                medicos.map(medico => (
                    <MedicoCard
                        key={medico.id}
                        medico={medico}
                        citasDisponibles={citasActivas[medico.id]}
                    />
                ))
            ) : (
                <p>No se encontraron médicos con los filtros seleccionados.</p>
            )}
        </div>
    );
}

export default ListaMedicos;