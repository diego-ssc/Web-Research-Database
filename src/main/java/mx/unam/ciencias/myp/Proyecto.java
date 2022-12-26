package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Clase que representa la tabla de proyectos
 * en la base de datos.
 *
 */
@Entity
@Table(name = "proyectos")
public class Proyecto implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id_proyecto")
    private Integer id;

    private String nombre;

    @ManyToMany(mappedBy = "proyectos", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Usuario> usuarios = new HashSet<>();

    private String mes;

    private String ano;

    @Transient
    private String cadenaUsuarios;

    public Integer getIdProyecto() {
        return id;
    }

    public void setIdProyecto(Integer id) {
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

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }
}
