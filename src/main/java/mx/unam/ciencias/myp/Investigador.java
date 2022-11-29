package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Clase que representa la tabla de investigadores
 * en la base de datos.
 *
 */
@Entity
@Table(name = "investigadores")
public class Investigador implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_investigador", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_area", referencedColumnName = "id_area")
    private AreaTrabajo areaTrabajo;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public AreaTrabajo getArea() {
        return areaTrabajo;
    }

    public void setAreaTrabajo(AreaTrabajo areaTrabajo) {
        this.areaTrabajo = areaTrabajo;
    }
}
