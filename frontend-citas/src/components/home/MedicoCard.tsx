import type {BloqueCita} from '../../types/types.ts';
import { Link } from "react-router-dom";
import { usePopups } from "../../context/PopupContext.tsx";
import type {ConfirmCitaResponseDTO, MedicoInfoDTO} from "../../types/citaTypes.ts";

interface MedicoCardProps {
    medico: MedicoInfoDTO;
    citasDisponibles?: BloqueCita[]; // Las citas son opcionales
}

const MedicoCard = ({ medico, citasDisponibles }: MedicoCardProps) => {
    const { abrirCitaPopup } = usePopups();

    const handleAgendarClick = ({cita, medico}: ConfirmCitaResponseDTO) => {
        abrirCitaPopup({
            cita,
            medico
        })
    }

    return (
        <div className="appointment">
            <Link to={`/perfil-medico/${medico.id}`} className="medico-link">
                <div className="row-container-home">
                    <div className="block1">
                        <img src="../../../public/img/medical-logo.jpg" alt={`Foto de ${medico.nombre}`} />
                        <h2>{`${medico.nombre} ${medico.apellido} - ₡${medico.costoConsulta}`}</h2>
                    </div>
                    <div className="block2">
                        <p><strong>Especialidad:</strong> <span>{medico.especialidad}</span></p>
                        <p><strong>Localidad:</strong> <span>{medico.localidad}</span></p>
                    </div>
                </div>
            </Link>
            {citasDisponibles && citasDisponibles.length > 0 && (
                <div className="citas-container">
                    {citasDisponibles.map((block, index) => (
                        <div key={index} className="block3">
                            <div className="date-item">
                                <span>{block.date}</span>
                            </div>
                            <div className="times">
                                {block.citas.map(cita => (
                                    <button key={cita.id} onClick={() => handleAgendarClick({cita, medico})} className="time-item">
                                        <span>{cita.horaInicio}</span>
                                    </button>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default MedicoCard;