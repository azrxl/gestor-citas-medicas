package cr.ac.una.logic.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "medico")
public class Medico {
    @Id
    @Column(name = "medico_id", nullable = false, length = 10)
    private String medicoId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Usuario usuario;

    @Column(name = "costoconsulta", length = 6)
    private String costoconsulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localidad")
    private Localidad localidad;

    @Column(name = "horariosemanal", length = 99)
    private String horariosemanal;

    @Column(name = "frecuencia")
    private Integer frecuencia;

    public String getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(String medicoId) {
        this.medicoId = medicoId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCostoconsulta() {
        return costoconsulta;
    }

    public void setCostoconsulta(String costoconsulta) {
        this.costoconsulta = costoconsulta;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public String getHorariosemanal() {
        return horariosemanal;
    }

    public void setHorariosemanal(String horariosemanal) {
        this.horariosemanal = horariosemanal;
    }

    public Integer getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(Integer frecuencia) {
        this.frecuencia = frecuencia;
    }

}