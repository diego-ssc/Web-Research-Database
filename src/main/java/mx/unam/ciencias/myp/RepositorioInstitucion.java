package mx.unam.ciencias.myp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Institucion;
import java.util.List;

@Repository
public interface RepositorioInstitucion extends CrudRepository<Institucion, Integer> {
    @Query("SELECT u FROM Institucion u WHERE u.nombre = ?1")
    public Institucion buscarPorNombre(String nombre);

    @Query("SELECT i FROM Institucion i WHERE i.nombre LIKE %?1%")
    public List<Institucion> buscarInstitucionesPorNombre(String nombre);
}
