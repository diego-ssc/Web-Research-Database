package mx.unam.ciencias.myp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.Assert.assertTrue;


@DataJpaTest
class MypApplicationTests {
    @Test
    void contextLoads() {
    }

    @Autowired
    RepositorioUsuario repositorio;
    

    @Test
    void testSave() {
        Iterable<Usuario> usuarios = repositorio.findAll();
        int i = 0;
        for (Usuario u : usuarios) {
            i++;
        }
        assertTrue(i == 0);

        Usuario usuario = new Usuario();
        repositorio.save(usuario);
        usuarios = repositorio.findAll();
        for (Usuario u : usuarios) {
            assertTrue(u == usuario);
            return;
        }
        assertTrue(false);
    }

    @Test
    void testFindAll() {
        Iterable<Usuario> usuarios = repositorio.findAll();
        int i = 0;
        for (Usuario u : usuarios) {
            i++;
        }
        assertTrue(i == 0);

        Usuario[] arreglo = new Usuario[100];
        for (int e = 0; e < 100; e++) {
            arreglo[e] = new Usuario();
            repositorio.save(arreglo[e]);
        }
        usuarios = repositorio.findAll();
        for (Usuario u : usuarios) {
            assertTrue(u == arreglo[i]);
            i++;
        }
        assertTrue(i == 100);
    }


}
