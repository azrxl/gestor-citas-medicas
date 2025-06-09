package cr.ac.una.demologinspringboot.dto.entities;

import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicoDTO {
    private Long id;
    private String login;
    private String nombre;
    private String apellidos;
    private boolean aprobado;
    private String especialidad;
    private BigDecimal costoConsulta;
    private String localidad;
    private String horarioSemanal;
    private Integer frecuenciaCita;

    public MedicoDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.login = usuario.getLogin();
        this.nombre = usuario.getNombre();
        this.apellidos = usuario.getApellido();
        this.aprobado = usuario.getAprobado();
        this.especialidad = usuario.getEspecialidad();
        this.costoConsulta = usuario.getCostoConsulta();
        this.localidad = usuario.getLocalidad();
        this.horarioSemanal = usuario.getHorarioSemanal();
        this.frecuenciaCita = usuario.getFrecuenciaCita();
    }
}
