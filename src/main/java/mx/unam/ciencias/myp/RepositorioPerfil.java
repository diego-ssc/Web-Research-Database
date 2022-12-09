package mx.unam.ciencias.myp;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Perfil;

@Repository("repositorioperfil")
public interface RepositorioPerfil extends CrudRepository<Perfil, Serializable> {
}
