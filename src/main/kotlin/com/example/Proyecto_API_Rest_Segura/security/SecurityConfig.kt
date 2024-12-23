package com.example.Proyecto_API_Rest_Segura.security

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {


    @Autowired
    private lateinit var rsaKeys: RSAKeysProperties

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {


        return http
            .csrf { it.disable() } // CROSS-SITE FORGERY
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.GET,"/pokemon/").permitAll()
                    .requestMatchers(HttpMethod.POST,"/pokemon/pokemon").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST,"/pokemon/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/pokemon/{id}").permitAll()
                    .requestMatchers(HttpMethod.PUT,"/pokemon/{id}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST,"/pokemon/{id}").hasRole("ADMIN")
                    .requestMatchers("/pokemon/tipo/{tipo}").permitAll()
                    .requestMatchers("/pokemon/generacion/{generacion}").permitAll()

                    .requestMatchers(HttpMethod.GET,"/movimientos/").permitAll()
                    .requestMatchers(HttpMethod.POST,"/movimientos/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST,"/movimientos/movimientos").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/movimientos/{id}").permitAll()
                    .requestMatchers(HttpMethod.PUT,"/movimientos/{id}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST,"/movimientos/{id}").hasRole("ADMIN")
                    .requestMatchers("/movimientos/tipo/{tipo}").permitAll()
                    .requestMatchers("/movimientos/categoria/{categoria}").permitAll()
                    .requestMatchers("/movimientos/categoria/{categoria}/tipo/{tipo}").permitAll()

                    .requestMatchers("/usuarios/register").permitAll()
                    .requestMatchers("/usuarios/login").permitAll()
                    .requestMatchers(HttpMethod.PUT,"/usuarios/usuario").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/usuarios/{id}").hasRole("ADMIN")
                    .anyRequest().authenticated()

            } // LOS RECURSOS PROTEGIDO Y PUBLICOS
            .oauth2ResourceServer { oauth2 -> oauth2.jwt(Customizer.withDefaults()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } //
            .httpBasic(Customizer.withDefaults())
            .build()

    }


    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


    @Bean
    fun authenticationManager(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }



    @Bean
    fun jwtEncoder(): JwtEncoder {
        val jwk: JWK = RSAKey.Builder(rsaKeys.publicKey).privateKey(rsaKeys.privateKey).build()
        val jwks: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }


    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey).build()
    }
}