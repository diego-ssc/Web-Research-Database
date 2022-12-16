package mx.unam.ciencias.myp;

import org.springframework.data.repository.CrudRepository;

import org.springframework.data.jpa.repository.Query;

import mx.unam.ciencias.myp.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUsuario extends CrudRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u WHERE u.email = ?1")
    public Usuario buscarPorEmail(String email);
}
