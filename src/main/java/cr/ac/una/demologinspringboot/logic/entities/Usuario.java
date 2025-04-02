package cr.ac.una.demologinspringboot.logic.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @NotBlank(message = "El login es obligatorio")
    @Column(name = "login", nullable = false, length = 50)
    private String login;

    @Size(max = 255)
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 50)
    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Size(max = 50)
    @NotBlank(message = "El apellido es obligatorio")
    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Size(max = 20)
    @NotBlank(message = "La cédula es obligatoria")
    @Column(name = "cedula", nullable = false, length = 20)
    private String cedula;

    @NotBlank(message = "El rol es obligatorio")
    @Column(name = "rol", nullable = false, length = 20)
    private String rol; // Ahora es un String

    @Size(max = 100)
    @Column(name = "especialidad", length = 100)
    private String especialidad;

    @Column(name = "costo_consulta", precision = 10, scale = 2)
    private BigDecimal costoConsulta;

    @Size(max = 100)
    @Column(name = "localidad", length = 100)
    private String localidad;

    @Size(max = 100)
    @Column(name = "horario_semanal")
    private String horarioSemanal;

    @Column(name = "frecuencia_cita")
    private int frecuenciaCita;
}