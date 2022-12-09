package mx.unam.ciencias.myp;

import javax.persistence.*;

/**
 * Clase que representa la tabla de descripciones
 * de artículos en la base de datos.
 *
 */
@Entity
@Table(name = "descripcion_articulos")
public class DescripcionArticulo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_descripcion")
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
