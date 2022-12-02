package mx.unam.ciencias.myp;

import javax.persistence.*;

/**
 * Clase que representa la tabla de proyectos
 * en la base de datos.
 *
 */
@Entity
@Table(name = "proyectos")
public class Proyecto {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id_proyecto")
    private Integer id;

    private String nombre;

    public Integer getIdProyecto() {
        return id;
    }

    public void setIdProyecto(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
