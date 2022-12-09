package mx.unam.ciencias.myp;

import org.springframework.boot.*;
import org.springframework.stereotype.*;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MypApplication implements CommandLineRunner {
    @Autowired
    RepositorioPerfil repositorioPerfil;
    public static void main(String[] args) {
        SpringApplication.run(MypApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Perfil indefinido = new Perfil();
        indefinido.setId(0);
        indefinido.setDescripcion("indefinido");
        
        Perfil investigador = new Perfil();
        investigador.setId(1);
        investigador.setDescripcion("investigador");

        Perfil estudiante = new Perfil();
        estudiante.setId(2);
        estudiante.setDescripcion("estudiante");
        
        Perfil general = new Perfil();
        general.setId(3);
        general.setDescripcion("general");

        Perfil administrador = new Perfil();
        administrador.setId(4);
        administrador.setDescripcion("administrador");

        repositorioPerfil.save(indefinido);
        repositorioPerfil.save(investigador);
        repositorioPerfil.save(estudiante);
        repositorioPerfil.save(general);
        repositorioPerfil.save(administrador);
    }
}
