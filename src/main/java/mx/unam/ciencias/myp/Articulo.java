package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile; // subir Archivo

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Clase que representa la tabla de instituciones
 * en la base de datos.
 *
 */
@Entity
@Table(name = "articulos")
public class Articulo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_articulo")
    private Integer id;

    private String nombre;

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
    // @Lob
    private MultipartFile archivo;

    @Transient
    private String cadenaUsuarios;

    private String url;

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

    public void setArchivo(MultipartFile archivo){
        this.archivo = archivo;
    }

    public MultipartFile getArchivo(){
        return archivo;
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


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getCadenaUsuarios() {
        return cadenaUsuarios;
    }

    public void setCadenaUsuarios(String cadenaUsuarios) {
        this.cadenaUsuarios = cadenaUsuarios;
    }

    @Override
    public String toString() {
        return this.id + "::" + this.nombre;
    }
}
