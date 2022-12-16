package mx.unam.ciencias.myp;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Articulo;

@Repository("repositorioarticulo")
public interface RepositorioArticulo extends CrudRepository<Articulo, Serializable> {

    @Query("SELECT u FROM Articulo u WHERE u.id = ?1")
    public Articulo buscarPorId(int id);
}
