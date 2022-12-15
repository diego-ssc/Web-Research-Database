package mx.unam.ciencias.myp;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.stereotype.*;
import org.springframework.boot.*;

@EnableAdminServer
@SpringBootApplication
public class MypApplication {    
    public static void main(String[] args) {
        SpringApplication.run(MypApplication.class, args);
    }
}
