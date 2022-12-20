package mx.unam.ciencias.myp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Perfil;

@Repository
public interface RepositorioProyecto extends CrudRepository<Proyecto, Integer> {
}
