package mx.unam.ciencias.myp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Institucion;

@Repository
public interface RepositorioInstitucion extends CrudRepository<Institucion, Integer> {
    @Query("SELECT u FROM Institucion u WHERE u.nombre = ?1")
    public Institucion buscarPorNombre(String nombre);
}
