package mx.unam.ciencias.myp;

import javax.persistence.*;

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
    @Column(name = "id_usuario",
            columnDefinition = "integer default 1")

    private Integer id;

    private String nombre;

    private String apellido;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "institucion", referencedColumnName = "nombre")
    private Institucion institucion;

    private String email;

    private String contrasena;
    
    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "perfil", referencedColumnName = "id_perfil")
    private Perfil perfil;

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



    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
