package ch.supsi.ticket.config;

import ch.supsi.ticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        .loginPage("/tickets/login")
                        .failureUrl("/tickets/login?error")
                        .defaultSuccessUrl("/tickets", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/tickets/logout")
                        .logoutSuccessUrl("/tickets/")
                        .permitAll())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/tickets/new").authenticated()
                        .requestMatchers( "/tickets", "/tickets/*", "/tickets/register").permitAll()
                        .requestMatchers("/tickets/login").permitAll()
                        .requestMatchers("/CSS/*", "/js/*","/CSS/ticket.html", "/images/**", "/aside.html").permitAll() //risorse statiche
                        .requestMatchers("/webjars/*").permitAll()
                        .requestMatchers("/tickets/*/edit").hasRole("ADMIN")
                        .requestMatchers("/tickets/search").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
    @Bean
    PasswordEncoder BCPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
