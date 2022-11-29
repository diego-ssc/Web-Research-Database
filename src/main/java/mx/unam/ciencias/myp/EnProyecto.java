package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Clase que representa la tabla de pertenencia
 * a proyecto en la base de datos.
 *
 */
@IdClass(EnProyectoPK.class)
@Entity
@Table(name = "in_proyecto")
public class EnProyecto {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_proyecto", referencedColumnName = "id_proyecto")
    private Proyecto proyecto;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}
