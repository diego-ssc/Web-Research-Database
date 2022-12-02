package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Clase que representa la tabla de pertenencia
 * a revista en la base de datos.
 *
 */
@IdClass(EnRevistaPK.class)
@Entity
@Table(name = "in_revista")
public class EnRevista {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_revista", referencedColumnName = "id_revista")
    private Revista revista;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Revista getRevista() {
        return revista;
    }

    public void setRevista(Revista revista) {
        this.revista = revista;
    }
}
