package mx.unam.ciencias.myp;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Articulo;
import java.util.List;

@Repository("repositorioarticulo")
public interface RepositorioArticulo extends CrudRepository<Articulo, Integer> {
    @Query("SELECT a FROM Articulo a WHERE a.nombre LIKE %?1%")
    public List<Articulo> buscarArticulosPorNombre(String nombre);
}
