package mx.unam.ciencias.myp;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.*;

@Configuration
public class DatabaseLoader {

    private RepositorioPerfil repositorioPerfil;
    private RepositorioInstitucion repositorioInstitucion;

    public DatabaseLoader(RepositorioPerfil repositorioPerfil,
                          RepositorioInstitucion repositorioInstitucion) {
        this.repositorioPerfil = repositorioPerfil;
        this.repositorioInstitucion = repositorioInstitucion;
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
                                                   fac, facps, fade, fae, fesac,
                                                   fesar, fesc, fesiz, fesza, faf,
                                                   fai, fam, famvz, famu, fao, fap,
                                                   faq));
        };
    }

}
