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
    private Integer area;

    private String descripcion;

    public Integer getIdArea() {
        return area;
    }

    public void setIdArea(Integer area) {
        this.area = area;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
