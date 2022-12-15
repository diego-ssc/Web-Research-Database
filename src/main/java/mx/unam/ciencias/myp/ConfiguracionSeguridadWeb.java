package mx.unam.ciencias.myp;

import javax.sql.DataSource;

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
import de.codecentric.boot.admin.config.AdminServerProperties;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridadWeb extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    private final AdminServerProperties servidorAdministrador;

    public ConfiguracionSeguridadWeb(AdminServerProperties servidorAdministrador) {
        this.servidorAdministrador = servidorAdministrador;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new ServicioInformacionUsuario();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/registered").authenticated()
            .anyRequest().permitAll()
            .and()
            .formLogin()
            .loginPage("/login")
            .usernameParameter("email")
            .successHandler(administradorInicioSesion)
            //.defaultSuccessUrl(administradorInicioSesion)
            .permitAll()
            .and()
            .logout().logoutSuccessUrl("/").permitAll();
    }
    @Autowired private AdministradorInicioSesion administradorInicioSesion;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
            new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.servidorAdministrador.getContextPath() + "/");

        http.authorizeRequests()
            .antMatchers(this.servidorAdministrador.getContextPath() + "/assets/**").permitAll()
            .antMatchers(this.servidorAdministrador.getContextPath() + "/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage(this.servidorAdministrador.getContextPath() + "/login")
            .successHandler(successHandler)
            .and()
            .logout()
            .logoutUrl(this.servidorAdministrador.getContextPath() + "/logout")
            .and()
            .httpBasic()
            .and()
            .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringRequestMatchers(new AntPathRequestMatcher
                                     (this.servidorAdministrador.getContextPath() + 
                                      "/instances", HttpMethod.POST.toString()), 
                                     new AntPathRequestMatcher
                                     (this.servidorAdministrador.getContextPath() + 
                                      "/instances/*", HttpMethod.DELETE.toString()),
                                     new AntPathRequestMatcher
                                     (this.servidorAdministrador.getContextPath() + "/actuator/**"))
            .and()
            .rememberMe()
            .key(UUID.randomUUID().toString())
            .tokenValiditySeconds(1209600);
        return http.build();
    }
    
}
