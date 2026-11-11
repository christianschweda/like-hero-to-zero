package de.iu.likeherotozero.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration   // diese Klasse enthält Einstellungen für Spring
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                // 1. Wer darf was?
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("WISSENSCHAFTLER")   // nur mit Login und passender Rolle
                        .anyRequest().permitAll()                                  // alles andere: frei zugänglich
                )
                // 2. Login über ein eigenes Formular
                .formLogin(form -> form
                        .loginPage("/login")                                       // unsere eigene Login-Seite
                        .defaultSuccessUrl("/admin/emissionen", true)              // nach dem Login hierhin
                        .permitAll()
                )
                // 3. Abmelden erlauben (führt standardmäßig zu /login?logout)
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();   // Passwörter mit BCrypt hashen
    }
}