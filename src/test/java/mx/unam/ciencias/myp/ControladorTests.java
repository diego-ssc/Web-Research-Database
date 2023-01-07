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
import javax.persistence.*;


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

    @MockBean
    RepositorioRevista repoRevista;


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
