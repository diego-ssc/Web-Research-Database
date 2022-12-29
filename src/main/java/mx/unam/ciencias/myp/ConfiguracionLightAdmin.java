// package mx.unam.ciencias.myp;

// import org.lightadmin.api.config.LightAdmin;
// import org.lightadmin.core.config.LightAdminWebApplicationInitializer;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Import;
// import org.springframework.beans.factory.annotation.Autowired;

// import org.springframework.boot.web.servlet.ServletContextInitializer;
// import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// import org.springframework.web.context.AbstractContextLoaderInitializer;
// import org.springframework.web.context.WebApplicationContext;
// import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

// import javax.servlet.ServletContext;
// import javax.servlet.ServletException;


// @Configuration
// public class ConfiguracionLightAdmin {
//     @Autowired
//     private ServletContext servletContext;

//     @Bean
//     public LightAdmin lightAdmin() {
//         LightAdmin lightAdmin = LightAdmin.configure(servletContext);
//         lightAdmin.baseUrl("/administrator");
//         return lightAdmin;
//     }
// }
