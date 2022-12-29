package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Clase que representa la tabla de usuarios
 * en la base de datos.
 *
 */
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    private String nombre;

    private String apellido;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "institucion", referencedColumnName = "id_institucion")
    private Institucion institucion;

    private String email;

    private String contrasena;

    private String dia;

    private String mes;

    private String ano;

    @Transient
    private String perfilString;

    @Transient
    private String institucionString;

    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "perfil", referencedColumnName = "id_perfil")
    private Perfil perfil;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(joinColumns = {
            @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario",
                        nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "id_articulo", referencedColumnName = "id_articulo",
                        nullable = false, updatable = false)})
    @JsonBackReference
    private Set<Articulo> articulos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(joinColumns = {
            @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario",
                        nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "id_proyecto", referencedColumnName = "id_proyecto",
                        nullable = false, updatable = false)})
    @JsonBackReference
    private Set<Proyecto> proyectos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(joinColumns = {
            @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario",
                        nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "id_revista", referencedColumnName = "id_revista",
                        nullable = false, updatable = false)})
    @JsonBackReference
    private Set<Revista> revistas = new HashSet<>();

    public boolean hasRole(String roleName) {
        if (roleName == null)
            return false;
        return this.perfil.getDescripcion().equals(roleName);
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Institucion getInstitucion() {
        return institucion;

    }
    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
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

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public String getPerfilString() {
        return perfilString;
    }

    public void setPerfilString(String perfilString) {
        this.perfilString = perfilString;
    }

    public String getInstitucionString() {
        return institucionString;
    }

    public void setInstitucionString(String institucionString) {
        this.institucionString = institucionString;
    }

    public Set<Articulo> getArticulos() {
        return this.articulos;
    }

    public void setArticulos(Set<Articulo> articulos) {
        this.articulos = articulos;
    }

    public Set<Proyecto> getProyectos() {
        return this.proyectos;
    }

    public void setProyectos(Set<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public Set<Revista> getRevistas() {
        return this.revistas;
    }

    public void setRevistas(Set<Revista> revistas) {
        this.revistas = revistas;
    }

    /**
     * Método que nos dice si el usuario es igual al objeto recibido.
     * @param objeto el objeto a comparar.
     * @return true, si el usuario es igual al objeto recibido;
     *         false, en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;

        Usuario usuario = (Usuario)objeto;
        boolean a = id == usuario.getId();
        boolean b = email.equals(usuario.getEmail());

        return a && b;
    }

    @Override
    public String toString() {
        return this.id + "::" + this.nombre;
    }
}
