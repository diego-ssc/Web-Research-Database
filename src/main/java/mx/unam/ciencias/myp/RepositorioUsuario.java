package mx.unam.ciencias.myp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

import mx.unam.ciencias.myp.Usuario;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepositorioUsuario extends CrudRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u WHERE u.email = ?1")
    public Usuario buscarPorEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.nombre = ?1")
    public Usuario buscarPorNombre(String nombre);

    @Query("SELECT u FROM Usuario u WHERE CONCAT(u.nombre, ' ', u.apellido) LIKE %?1%")
    public List<Usuario> buscarUsuariosPorNombre(String nombre);
}
