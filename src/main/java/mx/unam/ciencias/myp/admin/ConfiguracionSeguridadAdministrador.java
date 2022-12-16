package mx.unam.ciencias.myp.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.core.annotation.Order;
import java.util.UUID;

import mx.unam.ciencias.myp.ServicioInformacionUsuario;

@Configuration
@EnableWebSecurity
@Order(1)
public class ConfiguracionSeguridadAdministrador extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService UserDetailsService() {
        return new ServicioInformacionUsuario();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoderAdministrator() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").permitAll();

        http.antMatcher("/administrator/**")
            .authorizeRequests().anyRequest().hasAuthority("administrador")
            .and()
            .formLogin()
            .loginPage("/administrator/login")
            .usernameParameter("email")
            .loginProcessingUrl("/administrator/login")
            .defaultSuccessUrl("/administrator/home")
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/administrator/logout")
            .logoutSuccessUrl("/");
    }
}
