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
	public RepositorioUsuario repositorio;

    @Test
    public void testAgregaNuevoUsuario() {
        Assert.assertTrue(true);
    }

    
} 
