package gr.hua.dit.greenride.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // για development: απενεργοποίηση csrf
                .csrf(csrf -> csrf.disable())

                // ποιες διευθύνσεις επιτρέπονται χωρίς login
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // H2 console χωρίς login
                        .anyRequest().permitAll()                     // ΟΛΑ χωρίς login (dev only)
                )

                //  για να εμφανίζεται σωστά το H2 console
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // χωρις default login form
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}

