package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Clase que representa la tabla de instituciones
 * en la base de datos.
 *
 */
@Entity
@Table(name = "instituciones")
public class Institucion implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_institucion", referencedColumnName = "institucion")
    private Usuario usuario;

    private String nombre;

    private String locacion;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocacion() {
        return locacion;
    }

    public void setLocacion(String locacion) {
        this.locacion = locacion;
    }
}
