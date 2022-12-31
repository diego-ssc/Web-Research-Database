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

 @SpringBootTest
@ContextConfiguration(classes=MypApplication.class)
public class ControladorTests {

    @Autowired
    ControladorWeb controlador;

    @MockBean
    RepositorioUsuario repositorio;

    @Test
     public void testGetUsuarios() {

        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        org.mockito.Mockito.when(repositorio.findAll()).thenReturn(usuarios);

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
}
