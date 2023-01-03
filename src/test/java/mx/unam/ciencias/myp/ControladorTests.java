package mx.unam.ciencias.myp;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.Assert.assertTrue;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import static org.junit.Assert.assertEquals;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.LinkedList;


 @SpringBootTest
@ContextConfiguration(classes=MypApplication.class)
public class ControladorTests {

    @Autowired
    ControladorWeb controlador;

    @MockBean
    RepositorioUsuario repoUsuario;

    @MockBean
    RepositorioPerfil repoPerfil;

    @MockBean
    RepositorioInstitucion repoInst;

    @MockBean
    RepositorioArticulo repoArticulo;

    @Test
     public void testGetUsuarios() {

        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        org.mockito.Mockito.when(repoUsuario.findAll()).thenReturn(usuarios);

        int i = 0;
        for (Usuario u : controlador.getUsuarios()) {
            i++;
        }
        assertTrue(i == 0);

        Usuario[] arreglo = new Usuario[100];
        for (int e = 0; e < 100; e++) {
            arreglo[e] = new Usuario();
            usuarios.add(arreglo[e]);
        }
        for (Usuario u : controlador.getUsuarios()) {
            assertTrue(u == arreglo[i]);
            i++;
        }
        assertTrue(i == 100);
    }

    @Test
    public void testAgregaNuevoUsuario() {
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        org.mockito.Mockito.when(repoUsuario.save(any())).then(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuarios.add(usuario);
            return usuario;
        });

        Perfil perfil = new Perfil();
        Optional<Perfil> perfilOpt = Optional.of(perfil);
        org.mockito.Mockito.when(repoPerfil.findById(any())).thenReturn(perfilOpt);

        Institucion institucion = new Institucion();
        Optional<Institucion> institucionOpt = Optional.of(institucion);
        org.mockito.Mockito.when(repoInst.findById(any())).thenReturn(institucionOpt);

        int i = 0;
        Usuario[] arreglo = new Usuario[50];
        for (int e = 0; e < 50; e++) {
            arreglo[e] = creaUsuario();
            controlador.agregaNuevoUsuario(arreglo[e]);
        }
        for (Usuario u : usuarios) {
            assertTrue(u == arreglo[i]);
            i++;
        }
        assertTrue(i == 50);


        assertTrue(arreglo[0].getPerfil() == perfil);
        assertTrue(arreglo[0].getInstitucion() == institucion);

        i = 0;
        for (Usuario u : institucion.getUsuarios()) {
            assertTrue(arreglo[i] == u);
            i++;
        }
        assertTrue(i == 50);

    }

    private Usuario creaUsuario() {
        Usuario usuario = new Usuario();
        usuario.setPerfilString("999");
        usuario.setInstitucionString("888");
        usuario.setContrasena("contrasena");
        usuario.setDia("18");
        usuario.setMes("03");
        usuario.setAno("2003");

        return usuario;
    }

    @Test
     public void testGetArticulos() {

        ArrayList<Articulo> articulos = new ArrayList<Articulo>();

        org.mockito.Mockito.when(repoArticulo.findAll()).thenReturn(articulos);

        int i = 0;
        for (Articulo a : controlador.getArticulos()) {
            i++;
        }
        assertTrue(i == 0);

        Articulo[] arreglo = new Articulo[100];
        for (int e = 0; e < 100; e++) {
            arreglo[e] = new Articulo();
            articulos.add(arreglo[e]);
        }
        for (Articulo a : controlador.getArticulos()) {
            assertTrue(a == arreglo[i]);
            i++;
        }
        assertTrue(i == 100);
    }
}
