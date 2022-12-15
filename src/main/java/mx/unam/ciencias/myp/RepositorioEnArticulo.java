package mx.unam.ciencias.myp;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Articulo;

@Repository("repositorioenarticulo")
public interface RepositorioEnArticulo extends CrudRepository<EnArticulo, Serializable>{
    public Iterable<EnArticulo> findByArticulo(Articulo articulo);
}
