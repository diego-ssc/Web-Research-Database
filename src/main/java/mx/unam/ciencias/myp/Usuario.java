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
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id_usuario")
    private Integer id;

    private String nombre;

    private Integer institucion;

    private String email;

    private String fechaNacimiento;

    private String apellido;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
