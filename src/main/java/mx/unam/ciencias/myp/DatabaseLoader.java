package mx.unam.ciencias.myp;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import javax.sql.DataSource;
import org.hibernate.Hibernate;

@Configuration
public class DatabaseLoader {

    private RepositorioPerfil repositorioPerfil;
    private RepositorioInstitucion repositorioInstitucion;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioAreaTrabajo repositorioAreaTrabajo;

    @Autowired
    private DataSource dataSource;

    public DatabaseLoader(RepositorioPerfil repositorioPerfil,
                          RepositorioInstitucion repositorioInstitucion,
                          RepositorioUsuario repositorioUsuario,
                          RepositorioAreaTrabajo repositorioAreaTrabajo) {
        this.repositorioPerfil = repositorioPerfil;
        this.repositorioInstitucion = repositorioInstitucion;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioAreaTrabajo = repositorioAreaTrabajo;
    }

    @Bean
    public CommandLineRunner initializeDatabase() {
        return args -> {
            Perfil indefinido = new Perfil(1, "indefinido");
            Perfil investigador = new Perfil(2, "investigador");
            Perfil estudiante = new Perfil(3, "estudiante");
            Perfil administrador = new Perfil(4, "administrador");
            repositorioPerfil.saveAll(List.of(indefinido, investigador,
                                              estudiante, administrador));

            Institucion indefinidoInstitucion = new Institucion
                (1, "indefinido", "indefinido");
            Institucion fa = new Institucion
                (2, "Facultad de Arquitectura", "México");
            Institucion fad = new Institucion
                (3, "Facultad de Artes y Diseño", "México");
            Institucion fac = new Institucion
                (4, "Facultad de Ciencias", "México");
            Institucion facps = new Institucion
                (5, "Facultad de Ciencias Políticas y Sociales", "México");
            Institucion faca = new Institucion
                (6, "Facultad de Contaduría y Administración", "México");
            Institucion fade = new Institucion
                (7, "Facultad de Derecho", "México");
            Institucion fae = new Institucion
                (8, "Facultad de Economía", "México");
            Institucion fesac = new Institucion
                (9, "Facultad de Estudios Superiores (FES) Acatlán", "México");
            Institucion fesar = new Institucion
                (10, "Facultad de Estudios Superiores (FES) Aragón", "México");
            Institucion fesc = new Institucion
                (11, "Facultad de Estudios Superiores (FES) Cuautitlán", "México");
            Institucion fesiz = new Institucion
                (12, "Facultad de Estudios Superiores (FES) Iztacala", "México");
            Institucion fesza = new Institucion
                (13, "Facultad de Estudios Superiores (FES) Zaragoza", "México");
            Institucion faf = new Institucion
                (14, "Facultad de Filosofía y Letras", "México");
            Institucion fai = new Institucion
                (15, "Facultad de Ingeniería", "México");
            Institucion fam = new Institucion
                (16, "Facultad de Medicina", "México");
            Institucion famvz = new Institucion
                (17, "Facultad de Medicina Veterinaria y Zootecnia", "México");
            Institucion famu = new Institucion
                (18, "Facultad de Música", "México");
            Institucion fao = new Institucion
                (19, "Facultad de Odontología", "México");
            Institucion fap = new Institucion
                (20, "Facultad de Psicología", "México");
            Institucion faq = new Institucion
                (21, "Facultad de Química", "México");
            repositorioInstitucion.saveAll(List.of(indefinidoInstitucion, fa, fad,
                                                   fac, facps, faca, fade, fae, fesac,
                                                   fesar, fesc, fesiz, fesza, faf,
                                                   fai, fam, famvz, famu, fao, fap,
                                                   faq));

            AreaTrabajo indefinidoArea = new AreaTrabajo(1, "Indefinido");
            AreaTrabajo biologia = new AreaTrabajo(2, "Biología");
            AreaTrabajo fisica = new AreaTrabajo(3, "Física");
            AreaTrabajo cc = new AreaTrabajo(4, "Ciencias de la Computación");
            AreaTrabajo matem = new AreaTrabajo(5, "Matemáticas");
            AreaTrabajo actuaria = new AreaTrabajo(6, "Actuaría");
            AreaTrabajo ct = new AreaTrabajo(7, "Ciencias de la Tierra");
            AreaTrabajo fil = new AreaTrabajo(8, "Filosofía");
            AreaTrabajo der = new AreaTrabajo(9, "Derecho");
            AreaTrabajo psi = new AreaTrabajo(10, "Psicología");
            AreaTrabajo med = new AreaTrabajo(11, "Medicina");
            AreaTrabajo mus = new AreaTrabajo(12, "Música");
            AreaTrabajo vet = new AreaTrabajo(13, "Veterinaria");
            AreaTrabajo ing = new AreaTrabajo(14, "Ingeniería");
            AreaTrabajo con = new AreaTrabajo(15, "Contaduría");
            AreaTrabajo cp = new AreaTrabajo(16, "Ciencias Políticas y Sociales");
            AreaTrabajo ar = new AreaTrabajo(17, "Arquitectura");
            repositorioAreaTrabajo.saveAll(List.of(indefinidoArea, biologia,
                                                   fisica, cc, matem, actuaria,
                                                   ct, fil, der, psi, med, mus,
                                                   vet, ing, con, cp, ar));
        };
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {
        ResourceDatabasePopulator usuarios = new ResourceDatabasePopulator
            (false, false, "UTF-8", new ClassPathResource("sql_scripts/1_usuarios.sql"));
        usuarios.execute(dataSource);

        ResourceDatabasePopulator articulos = new ResourceDatabasePopulator
            (false, false, "UTF-8", new ClassPathResource("sql_scripts/2_articulos.sql"));
        articulos.execute(dataSource);

        ResourceDatabasePopulator proyectos = new ResourceDatabasePopulator
            (false, false, "UTF-8", new ClassPathResource("sql_scripts/3_proyectos.sql"));
        proyectos.execute(dataSource);

        ResourceDatabasePopulator revistas = new ResourceDatabasePopulator
            (false, false, "UTF-8", new ClassPathResource("sql_scripts/4_revistas.sql"));
        revistas.execute(dataSource);

        ResourceDatabasePopulator articulos_usuarios = new ResourceDatabasePopulator
            (false, false, "UTF-8", new ClassPathResource("sql_scripts/articulos_usuarios.sql"));
        articulos_usuarios.execute(dataSource);

        ResourceDatabasePopulator instituciones_usuarios = new ResourceDatabasePopulator
            (false, false, "UTF-8", new ClassPathResource("sql_scripts/instituciones_usuarios.sql"));
        instituciones_usuarios.execute(dataSource);

        ResourceDatabasePopulator proyectos_usuarios = new ResourceDatabasePopulator
            (false, false, "UTF-8", new ClassPathResource("sql_scripts/proyectos_usuarios.sql"));
        proyectos_usuarios.execute(dataSource);

        ResourceDatabasePopulator revistas_articulos = new ResourceDatabasePopulator
            (false, false, "UTF-8", new ClassPathResource("sql_scripts/revistas_articulos.sql"));
        revistas_articulos.execute(dataSource);

        ResourceDatabasePopulator revistas_usuarios = new ResourceDatabasePopulator
            (false, false, "UTF-8", new ClassPathResource("sql_scripts/revistas_usuarios.sql"));
        revistas_usuarios.execute(dataSource);
    }

    private void agregaArticulosUsuarios() {
        Iterable<Usuario> usuarios = repositorioUsuario.findAll();
        Institucion institucion;
        for (Usuario u : usuarios) {
            institucion = u.getInstitucion();
            institucion.agregaUsuario(u);
        }
    }

}
