package mx.unam.ciencias.myp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.beans.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.junit.Assert;

import org.springframework.boot.test.context.SpringBootTest;

@DataJpaTest
class MypApplicationTests {

    @Autowired
	private RepositorioUsuario repositorio;

    private ControladorWeb controlador = new ControladorWeb();
    Usuario usuario = new Usuario();

    @Test
    public void testAgregaNuevoUsuario() {
        usuario.setPerfilString("999");
        usuario.setInstitucionString("888");
        usuario.setContrasena("contrasena");
        usuario.setDia("18");
        usuario.setMes("03");
        usuario.setAno("2003");

        Iterable<Usuario> usuarios = controlador.getUsuarios();
        int i = 0;
        for (Usuario u : usuarios) {
            i++;
        }
        Assert.assertTrue(i == 0);
        controlador.agregaNuevoUsuario(usuario);
        usuarios = controlador.getUsuarios();
        for (Usuario u : usuarios) {
            Assert.assertTrue(u == usuario);
            return;
        }
        Assert.assertTrue(false);

    }

    
} 
