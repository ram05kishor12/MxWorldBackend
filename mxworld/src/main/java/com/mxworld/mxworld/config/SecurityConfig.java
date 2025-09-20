// package com.mxworld.mxworld.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import static org.springframework.security.config.Customizer.withDefaults;


// @Configuration
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 // Disable CSRF
//                 .csrf(csrf -> csrf.disable())

//                 // Configure request authorization
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger UI public
//                         .anyRequest().authenticated() // All other endpoints require authentication
//                 )

//                 // Enable HTTP Basic Authentication
//                 .httpBasic(withDefaults());

//         return http.build();
//     }
// }

// package com.mxworld.mxworld.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;    
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf(csrf -> csrf.disable())       // disable CSRF
//             .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // allow all requests

//         return http.build();
//     }
// }

package com.mxworld.mxworld.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (needed for POST requests from Swagger/Rest Client)
            .csrf(csrf -> csrf.disable())

            // Configure request authorization
            .authorizeHttpRequests(auth -> auth
                // Swagger UI and API docs - public
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )

            // Enable HTTP Basic Authentication for APIs
            .httpBasic(httpBasic -> {});

        return http.build();
    }
}


