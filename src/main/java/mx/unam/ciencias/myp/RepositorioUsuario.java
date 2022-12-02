package mx.unam.ciencias.myp;

import org.springframework.data.repository.CrudRepository;

import mx.unam.ciencias.myp.Usuario;

public interface RepositorioUsuario extends CrudRepository<Usuario, Integer> {
}
