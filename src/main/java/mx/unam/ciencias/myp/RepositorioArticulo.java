package mx.unam.ciencias.myp;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Articulo;

@Repository("repositorioarticulo")
public interface RepositorioArticulo extends CrudRepository<Articulo, Serializable> {
    @Query("SELECT a FROM Articulo a WHERE a.url = ?1")
    public Articulo buscarPorUrl(String url);
}
