package com.Developer.DreamShop.security.config;

import com.Developer.DreamShop.security.jwt.AuthTokenFilter;
import com.Developer.DreamShop.security.jwt.JwtAuthEntryPoint;
import com.Developer.DreamShop.security.user.ShopUserDetailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class ShopConfig {


    private final ShopUserDetailService userDetailService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    private static final List<String> SECURED_URLS = List
            .of("/api/v1/CartItems/**", "/api/v1/Cart/**");

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(SECURED_URLS.toArray(String[]::new))
//                        .authenticated().anyRequest().permitAll()
//                        .anyRequest().authenticated()
//                );
//
//        http.authenticationProvider(daoAuthenticationProvider());
//        http.addFilterBefore(authTokenFilter(),UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//}
//

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.exceptionHandling(ex ->
                ex.authenticationEntryPoint(jwtAuthEntryPoint));

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/Auth/**").permitAll()

                // Swagger (optional)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // Protected URLs (your secured URLs)
                .requestMatchers(SECURED_URLS.toArray(new String[0])).authenticated()

                // Everything else -> permit
                .anyRequest().permitAll()
        );

        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}