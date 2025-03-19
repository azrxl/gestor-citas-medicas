package cr.ac.una.logic.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "permisos")
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permisos_id", nullable = false)
    private Integer id;

    @Column(name = "permisos_nombre", length = 45)
    private String permisosNombre;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermisosNombre() {
        return permisosNombre;
    }

    public void setPermisosNombre(String permisosNombre) {
        this.permisosNombre = permisosNombre;
    }

}