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

    /**
     * Método que nos dice si el investigador es igual al objeto recibido.
     * @param objeto el objeto a comparar.
     * @return true, si el investigador es igual al objeto recibido;
     *         false, en otro caso.
     *
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;

        Investigador investigador = (Investigador)objeto;

        return investigador.getUsuario().equals(usuario);
    }

    /**
     * Método que devuelve el valor de la llave de dispersión
     * del investigador.
     * @return la llave de dispersión.
     *
     */
    @Override public int hashCode() {
        return usuario.hashCode();
    }
}
