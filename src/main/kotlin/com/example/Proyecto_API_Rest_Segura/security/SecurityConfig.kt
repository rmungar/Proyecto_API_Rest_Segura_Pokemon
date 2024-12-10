package com.example.Proyecto_API_Rest_Segura.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {




    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {


        return http
            .csrf { it.disable() } // CROSS-SITE FORGERY
            .authorizeHttpRequests {
                it
                    .requestMatchers("/pokemon/").permitAll()
                    .anyRequest().authenticated()
            } // LOS RECURSOS PROTEGIDO Y PUBLICOS
            .oauth2ResourceServer { oauth2 -> oauth2.jwt(Customizer.withDefaults()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } //
            .httpBasic(Customizer.withDefaults())
            .build()
    }
}