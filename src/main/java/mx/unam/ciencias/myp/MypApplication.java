package mx.unam.ciencias.myp;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.boot.*;

@SpringBootApplication
public class MypApplication implements CommandLineRunner {
    @Autowired
    RepositorioPerfil repositorioPerfil;

    @Autowired
    RepositorioInstitucion repositorioInstitucion;
    
    public static void main(String[] args) {
        SpringApplication.run(MypApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Perfil indefinido = new Perfil();
        indefinido.setId(1);
        indefinido.setDescripcion("indefinido");

        Perfil investigador = new Perfil();
        investigador.setId(2);
        investigador.setDescripcion("investigador");

        Perfil estudiante = new Perfil();
        estudiante.setId(3);
        estudiante.setDescripcion("estudiante");

        Perfil general = new Perfil();
        general.setId(4);
        general.setDescripcion("general");

        Perfil administrador = new Perfil();
        administrador.setId(5);
        administrador.setDescripcion("administrador");

        repositorioPerfil.save(indefinido);
        repositorioPerfil.save(investigador);
        repositorioPerfil.save(estudiante);
        repositorioPerfil.save(general);
        repositorioPerfil.save(administrador);


        Institucion indefinidoInstitucion = new Institucion();
        indefinidoInstitucion.setId(1);
        indefinidoInstitucion.setNombre("indefinido");
        indefinidoInstitucion.setLocacion("indefinido");

        Institucion fa = new Institucion();
        fa.setId(2);
        fa.setNombre("Facultad de Arquitectura");
        fa.setLocacion("México");

        Institucion fad = new Institucion();
        fad.setId(3);
        fad.setNombre("Facultad de Artes y Diseño");
        fad.setLocacion("México");

        Institucion fac = new Institucion();
        fac.setId(4);
        fac.setNombre("Facultad de Ciencias");
        fac.setLocacion("México");

        Institucion facps = new Institucion();
        facps.setId(5);
        facps.setNombre("Facultad de Ciencias Políticas y Sociales");
        facps.setLocacion("México");

        Institucion faca = new Institucion();
        faca.setId(6);
        faca.setNombre("Facultad de Contaduría y Administración");
        faca.setLocacion("México");

        Institucion fade = new Institucion();
        fade.setId(7);
        fade.setNombre("Facultad de Derecho");
        fade.setLocacion("México");

        Institucion fae = new Institucion();
        fae.setId(8);
        fae.setNombre("Facultad de Economía");
        fae.setLocacion("México");

        Institucion fesac = new Institucion();
        fesac.setId(9);
        fesac.setNombre("Facultad de Estudios Superiores (FES) Acatlán");
        fesac.setLocacion("México");

        Institucion fesar = new Institucion();
        fesar.setId(10);
        fesar.setNombre("Facultad de Estudios Superiores (FES) Aragón");
        fesar.setLocacion("México");

        Institucion fesc = new Institucion();
        fesc.setId(11);
        fesc.setNombre("Facultad de Estudios Superiores (FES) Cuautitlán");
        fesc.setLocacion("México");

        Institucion fesiz = new Institucion();
        fesiz.setId(12);
        fesiz.setNombre("Facultad de Estudios Superiores (FES) Iztacala");
        fesiz.setLocacion("México");

        Institucion fesza = new Institucion();
        fesza.setId(13);
        fesza.setNombre("Facultad de Estudios Superiores (FES) Zaragoza");
        fesza.setLocacion("México");

        Institucion faf = new Institucion();
        faf.setId(14);
        faf.setNombre("Facultad de Filosofía y Letras");
        faf.setLocacion("México");

        Institucion fai = new Institucion();
        fai.setId(15);
        fai.setNombre("Facultad de Ingeniería");
        fai.setLocacion("México");

        Institucion fam = new Institucion();
        fam.setId(16);
        fam.setNombre("Facultad de Medicina");
        fam.setLocacion("México");

        Institucion famvz = new Institucion();
        famvz.setId(17);
        famvz.setNombre("Facultad de Medicina Veterinaria y Zootecnia");
        famvz.setLocacion("México");

        Institucion famu = new Institucion();
        famu.setId(18);
        famu.setNombre("Facultad de Música");
        famu.setLocacion("México");

        Institucion fao = new Institucion();
        fao.setId(19);
        fao.setNombre("Facultad de Odontología");
        fao.setLocacion("México");

        Institucion fap = new Institucion();
        fap.setId(20);
        fap.setNombre("Facultad de Psicología");
        fap.setLocacion("México");

        Institucion faq = new Institucion();
        faq.setId(21);
        faq.setNombre("Facultad de Química");
        faq.setLocacion("México");

        repositorioInstitucion.save(indefinidoInstitucion);
        repositorioInstitucion.save(fa);
        repositorioInstitucion.save(fad);
        repositorioInstitucion.save(fac);
        repositorioInstitucion.save(facps);
        repositorioInstitucion.save(fade);
        repositorioInstitucion.save(fae);
        repositorioInstitucion.save(fesac);
        repositorioInstitucion.save(fesar);
        repositorioInstitucion.save(fesc);
        repositorioInstitucion.save(fesiz);
        repositorioInstitucion.save(fesza);
        repositorioInstitucion.save(faf);
        repositorioInstitucion.save(fai);
        repositorioInstitucion.save(fam);
        repositorioInstitucion.save(famvz);
        repositorioInstitucion.save(famu);
        repositorioInstitucion.save(fao);
        repositorioInstitucion.save(fap);
        repositorioInstitucion.save(faq);
    }
}
