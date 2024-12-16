package com.example.Proyecto_API_Rest_Segura

import com.example.Proyecto_API_Rest_Segura.security.RSAKeysProperties
import com.example.Proyecto_API_Rest_Segura.services.MovimientoService
import com.example.Proyecto_API_Rest_Segura.services.PokemonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@SpringBootApplication
@EnableConfigurationProperties(RSAKeysProperties::class)
class ProyectoApiRestSeguraApplication

fun main(args: Array<String>) {
	runApplication<ProyectoApiRestSeguraApplication>(*args)
}


@Component
@Order(1)  // Ejecutar primero
class FirstRunner() : CommandLineRunner {
	@Autowired
	private lateinit var movimientoService: MovimientoService
	// Inyección de constructor
	override fun run(vararg args: String?) {
		movimientoService.populateMovimientosTable()
	}
}

@Component
@Order(2)  // Ejecutar después
class SecondRunner() : CommandLineRunner {

	@Autowired
	private lateinit var pokemonService: PokemonService
	override fun run(vararg args: String?) {
		pokemonService.poblatePokemonTable()
	}
}

