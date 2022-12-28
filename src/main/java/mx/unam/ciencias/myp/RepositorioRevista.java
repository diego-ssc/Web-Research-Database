package mx.unam.ciencias.myp;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Articulo;
import java.util.List;

@Repository("repositoriorevista")
public interface RepositorioRevista extends CrudRepository<Revista, Serializable> {
    @Query("SELECT r FROM Revista r WHERE r.nombre LIKE %?1%")
    public List<Revista> buscarPorNombre(String nombre);
}
