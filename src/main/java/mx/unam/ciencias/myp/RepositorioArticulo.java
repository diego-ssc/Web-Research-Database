package main.java.mx.unam.ciencias.myp;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import main.java.mx.unam.ciencias.myp.Articulo;

@Repository("repositorioarticulo")
public interface RepositorioArticulo extends JpaRepository<Articulo, Serializable> {

}
