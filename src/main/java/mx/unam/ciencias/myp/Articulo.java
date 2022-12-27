package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile; // subir Archivo

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
    private Set<Usuario> usuarios = new HashSet<>();

    private String descripcion;

    private String mes;

    private int ano;

    @Transient
    @Lob
    private MultipartFile archivo;

    @Transient
    private String cadenaUsuarios;

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

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCadenaUsuarios() {
        return cadenaUsuarios;
    }

    public void setCadenaUsuarios(String cadenaUsuarios) {
        this.cadenaUsuarios = cadenaUsuarios;
    }
}
