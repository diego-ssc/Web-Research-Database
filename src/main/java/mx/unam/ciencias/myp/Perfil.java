package mx.unam.ciencias.myp;

import javax.persistence.*;

/**
 * Clase que representa la tabla de perfiles
 * en la base de datos.
 *
 */
@Entity
@Table(name = "perfiles")
public class Perfil {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Integer id;
    
    private String descripcion;

    public Perfil() {}
    
    public Perfil(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }
    
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
