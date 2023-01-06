package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Clase que representa la tabla de revistas
 * en la base de datos.
 *
 */
@Entity
@Table(name = "revistas")
public class Revista implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_revista")
    private Integer id;

    private String nombre;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(joinColumns = {
            @JoinColumn(name = "id_revista", referencedColumnName = "id_revista",
                        nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario",
                        nullable = false, updatable = false)})
    @JsonBackReference
    private Set<Usuario> usuarios = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(joinColumns = {
            @JoinColumn(name = "id_revista", referencedColumnName = "id_revista",
                        nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "id_articulo", referencedColumnName = "id_articulo",
                        nullable = false, updatable = false)})
    @JsonBackReference
    private Set<Articulo> articulos = new HashSet<>();

    @Transient
    private String cadenaUsuarios;

    @Transient
    private String cadenaArticulos;

    private String mes;

    private String ano;

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

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Set<Articulo> getArticulos() {
        return this.articulos;
    }

    public void setArticulos(Set<Articulo> articulos) {
        this.articulos = articulos;
    }

    public String getCadenaUsuarios() {
        return cadenaUsuarios;
    }

    public void setCadenaUsuarios(String cadenaUsuarios) {
        this.cadenaUsuarios = cadenaUsuarios;
    }

    public String getCadenaArticulos() {
        return cadenaArticulos;
    }

    public void setCadenaArticulos(String cadenaArticulos) {
        this.cadenaArticulos = cadenaArticulos;
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

    public void agregaUsuario(Usuario usuario) {
        usuarios.add(usuario);
        usuario.getRevistas().add(this);
    }

    public void eliminaUsuario(Usuario usuario) {
        usuarios.remove(usuario);
        usuario.getRevistas().remove(this);
    }

    public void agregaArticulo(Articulo articulo) {
        articulos.add(articulo);
        articulo.getRevistas().add(this);
    }

    public void eliminaArticulo(Articulo articulo) {
        articulos.remove(articulo);
        articulo.getRevistas().remove(this);
    }
}
