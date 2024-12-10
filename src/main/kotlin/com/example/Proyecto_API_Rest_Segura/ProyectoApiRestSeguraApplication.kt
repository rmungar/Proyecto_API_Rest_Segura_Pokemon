package com.example.Proyecto_API_Rest_Segura

import com.example.Proyecto_API_Rest_Segura.security.RSAKeysProperties
import com.example.Proyecto_API_Rest_Segura.services.PokemonService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RSAKeysProperties::class)
class ProyectoApiRestSeguraApplication

fun main(args: Array<String>) {
	runApplication<ProyectoApiRestSeguraApplication>(*args)
}

