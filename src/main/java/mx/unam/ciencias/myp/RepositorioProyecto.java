package mx.unam.ciencias.myp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Perfil;
import java.util.List;

@Repository
public interface RepositorioProyecto extends CrudRepository<Proyecto, Integer> {
    @Query("SELECT p FROM Proyecto p WHERE p.nombre LIKE %?1%")
    public List<Proyecto> buscarProyectosPorNombre(String nombre);
}
