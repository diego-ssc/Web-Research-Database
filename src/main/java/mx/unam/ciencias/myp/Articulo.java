package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Clase que representa la tabla de instituciones
 * en la base de datos.
 *
 */
@Entity
@Table(name = "articulos")
public class Articulo implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id_articulo")
    private Integer id;

    private String nombre;

    private String url;

    @ManyToMany(mappedBy = "articulos", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Usuario> usuarios = new HashSet<>();

    @ManyToMany(mappedBy = "articulos", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Revista> revistas = new HashSet<>();

    private String descripcion;

    private String mes;

    private String ano;

    @Transient
    private String cadenaUsuarios;

    public Integer getIdArticulo() {
        return id;
    }

    public void setIdArticulo(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Set<Revista> getRevistas() {
        return this.revistas;
    }

    public void setRevistas(Set<Revista> revistas) {
        this.revistas = revistas;
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

    public String getCadenaUsuarios() {
        return cadenaUsuarios;
    }

    public void setCadenaUsuarios() {
        this.cadenaUsuarios = cadenaUsuarios;
    }
}
