package mx.unam.ciencias.myp;

import javax.persistence.*;

/**
 * Clase que representa la tabla de áreas de trabajo
 * en la base de datos.
 *
 */
@Entity
@Table(name = "areas_trabajo")
public class AreaTrabajo {
    @Id
    @Column(name = "id_area")
    private Integer id;

    private String descripcion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
