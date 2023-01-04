package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Clase que representa la tabla de instituciones
 * en la base de datos.
 *
 */
@Entity
@Table(name = "instituciones")
public class Institucion {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_institucion")
    private Integer id;

    private String nombre;

    private String locacion;

    @OneToMany(targetEntity=Usuario.class)
    @JsonBackReference
    private List<Usuario> usuarios = new ArrayList<>();

    @Transient
    private String cadenaUsuarios;

    @Transient
    private String cadenaUsuarios;

    public Institucion() {}

    public Institucion(Integer id, String nombre, String locacion) {
        this.id = id;
        this.nombre = nombre;
        this.locacion = locacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getCadenaUsuarios() {
        return cadenaUsuarios;
    }

    public void setCadenaUsuarios(String cadenaUsuarios) {
        this.cadenaUsuarios = cadenaUsuarios;
    }

    public void agregaUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
}
