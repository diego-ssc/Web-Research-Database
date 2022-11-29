package mx.unam.ciencias.myp;

import javax.persistence.*;

/**
 * Clase que representa la tabla de revistas
 * en la base de datos.
 *
 */
@Entity
@Table(name = "revistas")
public class Revista {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id_revista")
    private Integer id;

    private String nombre;

    public Integer getIdRevista() {
        return id;
    }

    public void setIdRevista(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
