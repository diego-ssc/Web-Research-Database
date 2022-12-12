package mx.unam.ciencias.myp;

import javax.persistence.*;

/**
 * Clase que representa la tabla de instituciones
 * en la base de datos.
 *
 */
@Entity
@Table(name = "articulos")
public class Articulo {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id_articulo")
    private Integer id;

    private String nombre;

    private String url;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "descripcion", referencedColumnName = "descripcion")
    private DescripcionArticulo descripcion;

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

    public DescripcionArticulo getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(DescripcionArticulo descripcion) {
        this.descripcion = descripcion;
    }
}
