package com.example.tasktrackerb7.configs.security;

import com.example.tasktrackerb7.configs.jwt.JwtTokenFilter;
import com.example.tasktrackerb7.db.repository.UserRepository;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebAppSecurity {

    @Bean
    AuthenticationProvider authenticationProvider(UserRepository userRepo) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService((email) -> userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user with email = " + email + " not found!")));
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtTokenFilter filter) throws Exception {
        httpSecurity.cors().and().csrf().disable().authorizeHttpRequests(auth -> {
            auth.antMatchers("api/auth/**").permitAll().antMatchers("/api-docs", "/v3/api-docs").permitAll().anyRequest().permitAll();
        });
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setExposedHeaders(Collections.singletonList("x-auth-token"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
        return new EmbeddedTomcatCustomizer();
    }

    private static class EmbeddedTomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

        @Override
        public void customize(TomcatServletWebServerFactory factory) {
            factory.addConnectorCustomizers(connector -> {
                connector.setProperty("relaxedPathChars", "<>[\\]^`{|}");
                connector.setProperty("relaxedQueryChars", "<>[\\]^`{|}");
            });
        }
    }
}
