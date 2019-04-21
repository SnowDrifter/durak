package ru.romanov.durak.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import ru.romanov.durak.user.service.UserService;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@ComponentScan("ru.romanov.durak")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.userDetailsService(userService);

        http.authorizeRequests().and().formLogin().loginPage("/login").permitAll().usernameParameter("username")
                .passwordParameter("password").failureUrl("/login?error=true");

        http.logout().permitAll().logoutSuccessUrl("/home");

        http.authorizeRequests().antMatchers("/lobby").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/edit").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/multiplayer").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/resources/**").permitAll();

        http.csrf().disable();

        http.rememberMe().tokenRepository(persistentTokenRepository()).tokenValiditySeconds(1209600);
    }

    @Bean(name = "bcrypt")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        tokenRepositoryImpl.setDataSource(dataSource);
        return tokenRepositoryImpl;
    }

}
