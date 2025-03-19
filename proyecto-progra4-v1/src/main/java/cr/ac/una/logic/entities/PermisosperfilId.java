package cr.ac.una.logic.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class PermisosperfilId implements java.io.Serializable {
    private static final long serialVersionUID = 7285725506067654176L;
    @Column(name = "perfil_id", nullable = false)
    private Integer perfilId;

    @Column(name = "permiso_id", nullable = false)
    private Integer permisoId;

    public Integer getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(Integer perfilId) {
        this.perfilId = perfilId;
    }

    public Integer getPermisoId() {
        return permisoId;
    }

    public void setPermisoId(Integer permisoId) {
        this.permisoId = permisoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PermisosperfilId entity = (PermisosperfilId) o;
        return Objects.equals(this.perfilId, entity.perfilId) &&
                Objects.equals(this.permisoId, entity.permisoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(perfilId, permisoId);
    }

}