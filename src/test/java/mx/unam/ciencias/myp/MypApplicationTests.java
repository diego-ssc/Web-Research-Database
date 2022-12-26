package mx.unam.ciencias.myp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class MypApplicationTests {
    @Test
    void contextLoads() {
    }

    @Autowired
    RepositorioUsuario repositorio;
    Usuario usuario = new Usuario();

    @Test
    void testSave() {

        Iterable<Usuario> i = repositorio.findAll();
        for (Usuario u : i) {
        }
    }


}
