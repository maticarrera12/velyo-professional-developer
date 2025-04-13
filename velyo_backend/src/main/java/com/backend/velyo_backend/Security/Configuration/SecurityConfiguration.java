package com.backend.velyo_backend.Security.Configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth

                        // Rutas públicas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/accommodations/**",
                                "/api/categories/**",
                                "/api/amenities/**",
                                "/api/bookings/**",
                                "/api/reviews/**"
                        ).permitAll()

                        // Rutas de usuario autenticado
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/**",
                                "/api/bookings/user"
                        ).authenticated()
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/bookings/confirm/",
                                "/api/users/update-name"
                        ).authenticated()
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/users/**"
                        ).authenticated()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users/add-favorite",
                                "/api/users/remove-favorite"
                        ).authenticated()

                        // Rutas para ADMIN
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/accommodations/**",
                                "/api/categories/**",
                                "/api/amenities/**",
                                "/api/users/**"
                        ).hasAuthority("ADMIN")
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/accommodations/**",
                                "/api/categories/**",
                                "/api/amenities/**",
                                "/api/users/**"
                        ).hasAuthority("ADMIN")
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/accommodations/**",
                                "/api/categories/**",
                                "/api/amenities/**",
                                "/api/users/**"
                        ).hasAuthority("ADMIN")

                        // Cualquier otra solicitud
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOrigin("http://localhost:5173");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
