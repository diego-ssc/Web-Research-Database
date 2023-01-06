package mx.unam.ciencias.myp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import mx.unam.ciencias.myp.Perfil;

@Repository
public interface RepositorioPerfil extends CrudRepository<Perfil, Integer> {
    @Query("SELECT p FROM Usuario p WHERE p.nombre = ?1")
    public Perfil buscarPorNombre(String nombre);
}

