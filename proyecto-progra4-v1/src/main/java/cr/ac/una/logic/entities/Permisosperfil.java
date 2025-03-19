package cr.ac.una.logic.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "permisosperfil")
public class Permisosperfil {
    @EmbeddedId
    private PermisosperfilId id;

    @MapsId("perfilId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    @MapsId("permisoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permiso_id", nullable = false)
    private Permiso permiso;

    public PermisosperfilId getId() {
        return id;
    }

    public void setId(PermisosperfilId id) {
        this.id = id;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Permiso getPermiso() {
        return permiso;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }

}