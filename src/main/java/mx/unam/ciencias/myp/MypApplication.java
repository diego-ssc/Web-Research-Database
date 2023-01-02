package mx.unam.ciencias.myp;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.*;
import org.springframework.boot.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.vaadin.flow.server.VaadinServlet;

/**
 * El punto de entrada de la aplicación.
 *
 */
@SpringBootApplication
public class MypApplication {
    public static void main(String[] args) {
        SpringApplication.run(MypApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean frontendServletBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean<>
            (new VaadinServlet() {
                    @Override
                    protected void service(HttpServletRequest req, HttpServletResponse resp)
                        throws ServletException, IOException {
                        if (!serveStaticOrWebJarRequest(req, resp)) {
                            resp.sendError(404);
                        }
                    }
                }, "/frontend/*");
        bean.setLoadOnStartup(1);
        return bean;
    }
}
