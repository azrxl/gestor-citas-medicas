package cr.ac.una.demologinspringboot.logic.service.citas;

import cr.ac.una.demologinspringboot.dto.cita.CitaConfirmDTO;
import cr.ac.una.demologinspringboot.dto.cita.ConfirmCitaResponseDTO;
import cr.ac.una.demologinspringboot.dto.cita.MedicoInfoDTO;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservarCitaService {
    private final CitaService citaService;
    private final UsuarioService usuarioService;

    @Autowired
    public ReservarCitaService(CitaService citaService, UsuarioService usuarioService) {
        this.citaService = citaService;
        this.usuarioService = usuarioService;
    }


    public ConfirmCitaResponseDTO getConfirmCita(Long citaId) {
        // 1) Buscar la cita
        Cita cita = citaService.findCitaByIdOrThrow(citaId);

        // 2) Buscar el médico asociado
        Usuario medico = usuarioService.findUsuarioByLoginOrThrow(cita.getLoginMedico());

        // 3) Mapear a DTOs
        CitaConfirmDTO citaDto = new CitaConfirmDTO();
        citaDto.setId(cita.getId());
        citaDto.setFecha(cita.getFecha());
        citaDto.setHoraInicio(cita.getHoraInicio());
        citaDto.setHoraFin(cita.getHoraFin());
        citaDto.setEstado(cita.getEstado());

        MedicoInfoDTO medicoDto = new MedicoInfoDTO();
        medicoDto.setId(medico.getId());
        medicoDto.setNombre(medico.getNombre());
        medicoDto.setApellido(medico.getApellido());
        medicoDto.setEspecialidad(medico.getEspecialidad());
        medicoDto.setLocalidad(medico.getLocalidad());
        medicoDto.setCostoConsulta(medico.getCostoConsulta());

        // 4) Ensamblar respuesta
        ConfirmCitaResponseDTO resp = new ConfirmCitaResponseDTO();
        resp.setCita(citaDto);
        resp.setMedico(medicoDto);
        return resp;
    }
}
