package mx.unam.ciencias.myp;

import javax.persistence.*;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

/**
 * Clase que representa la tabla de revistas
 * en la base de datos.
 *
 */
@Entity
@Table(name = "revistas")

public class Revista implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id_revista")
    private Integer id;

    private String nombre;


    @ManyToMany(mappedBy = "revistas", fetch = FetchType.LAZY)
    private Set<Usuario> usuarios = new HashSet<>();

    @Transient
    private String cadenaUsuarios;    

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


    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getCadenaUsuarios() {
        return cadenaUsuarios;
    }

    public void setCadenaUsuarios(String cadenaUsuarios) {
        this.cadenaUsuarios = cadenaUsuarios;
    }
}
