package mx.unam.ciencias.myp;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Articulo;
import java.util.List;

@Repository
public interface RepositorioArticulo extends CrudRepository<Articulo, Integer> {
    @Query("SELECT a FROM Articulo a WHERE a.url = ?1")
    public Articulo buscarPorUrl(String url);

    @Query("SELECT a FROM Articulo a WHERE a.nombre LIKE %?1%")
    public List<Articulo> buscarArticulosPorNombre(String nombre);
}
