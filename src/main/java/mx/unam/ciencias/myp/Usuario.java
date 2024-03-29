package mx.unam.ciencias.myp;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    private String contrasena;

    private String dia;

    private String mes;

    private String ano;

    @Transient
    private String perfilString;

    @Transient
    private String institucionString;

    @Transient
    private String cadenaArticulos;

    @Transient
    private String cadenaProyectos;

    @Transient
    private String cadenaRevistas;

    @Transient
    private String areaTrabajoString;

    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "perfil", referencedColumnName = "id_perfil")
    private Perfil perfil;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "area_trabajo", referencedColumnName = "id_area")
    private AreaTrabajo areaTrabajo;

    @ManyToMany(mappedBy = "usuarios", fetch = FetchType.LAZY,
                cascade = CascadeType.MERGE)
    @JsonManagedReference
    private Set<Articulo> articulos = new HashSet<>();

    @ManyToMany(mappedBy = "usuarios", fetch = FetchType.LAZY,
                cascade = CascadeType.MERGE)
    @JsonManagedReference
    private Set<Proyecto> proyectos = new HashSet<>();

    @ManyToMany(mappedBy = "usuarios", fetch = FetchType.LAZY,
                cascade = CascadeType.MERGE)
    @JsonManagedReference
    private Set<Revista> revistas = new HashSet<>();

    public boolean hasRole(String roleName) {
        if (roleName == null)
            return false;
        return this.perfil.getDescripcion().equals(roleName.toLowerCase());
    }
    
    private String email;

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

    public AreaTrabajo getAreaTrabajo() {
        return areaTrabajo;
    }

    public void setAreaTrabajo(AreaTrabajo areaTrabajo) {
        this.areaTrabajo = areaTrabajo;
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

    public String getCadenaArticulos() {
        return cadenaArticulos;
    }

    public void setCadenaArticulos(String cadenaArticulos) {
        this.cadenaArticulos = cadenaArticulos;
    }

    public String getCadenaRevistas() {
        return cadenaRevistas;
    }

    public void setCadenaRevistas(String cadenaRevistas) {
        this.cadenaRevistas = cadenaRevistas;
    }

    public String getCadenaProyectos() {
        return cadenaProyectos;
    }

    public String getAreaTrabajoString() {
        return areaTrabajoString;
    }

    public void setAreaTrabajoString(String areaTrabajoString) {
        this.areaTrabajoString = areaTrabajoString;
    }

    public void setCadenaProyectos(String cadenaProyecto) {
        this.cadenaProyectos = cadenaProyectos;
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
        return this.id.toString();
    }
}
