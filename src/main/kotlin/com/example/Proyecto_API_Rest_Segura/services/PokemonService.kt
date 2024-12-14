package com.example.Proyecto_API_Rest_Segura.services

import com.example.Proyecto_API_Rest_Segura.exception.FileNotFoundException
import com.example.Proyecto_API_Rest_Segura.exception.NotFoundException
import com.example.Proyecto_API_Rest_Segura.exception.ParameterException
import com.example.Proyecto_API_Rest_Segura.model.Pokemon
import com.example.Proyecto_API_Rest_Segura.repository.PokemonRepository
import com.example.Proyecto_API_Rest_Segura.utils.Tipos
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.File

@Service
class PokemonService {
    @Autowired
    private lateinit var pokemonRepository: PokemonRepository

    fun poblatePokemonTable(){
        try{
            val pokemonFilePath = "src/main/resources/static/pokemon.txt"
            val pokemonList = mutableListOf<Pokemon>()
            val file = File(pokemonFilePath)

            var name: String? = null
            var pokedexDescription: String? = null
            var types: List<String> = emptyList()
            var isLegendary = false
            var generation = 0
            var abilities: List<String> = emptyList()

            file.forEachLine { line ->
                val trimmedLine = line.trim()

                when {
                    trimmedLine.startsWith("[") && trimmedLine.endsWith("]") -> {
                        // Cuando encontramos una nueva entrada, almacenamos la anterior (si existe)
                        if (name != null && pokedexDescription != null) {
                            if (types.size > 1){
                                pokemonList.add(
                                    Pokemon(
                                        idPokemon = null,
                                        nombre = name ?: "",
                                        descripcion = pokedexDescription ?: "",
                                        tipo1 = types[0],
                                        tipo2 = types[1],
                                        legendario = isLegendary,
                                        generacion = generation,
                                        habilidad = abilities[0],
                                    )
                                )
                            }
                            else{
                                pokemonList.add(
                                    Pokemon(
                                        idPokemon = null,
                                        nombre = name ?: "",
                                        descripcion = pokedexDescription ?: "",
                                        tipo1 = types[0],
                                        tipo2 = null,
                                        legendario = isLegendary,
                                        generacion = generation,
                                        habilidad = abilities[0],
                                    )
                                )
                            }

                        }

                        // Reiniciamos los valores para el siguiente Pokémon
                        name = trimmedLine.removeSurrounding("[", "]")
                        pokedexDescription = null
                        types = emptyList()
                        isLegendary = false
                        generation = 0
                        abilities = emptyList()
                    }

                    trimmedLine.startsWith("Name =") -> name = trimmedLine.substringAfter("Name =").trim()

                    trimmedLine.startsWith("Pokedex =") -> pokedexDescription = trimmedLine.substringAfter("Pokedex =").trim()

                    trimmedLine.startsWith("Types =") -> types = trimmedLine.substringAfter("Types =").split(",").map { it.trim() }

                    trimmedLine.startsWith("Abilities =") -> abilities = trimmedLine.substringAfter("Abilities =").split(",").map { it.trim() }

                    trimmedLine.startsWith("Flags =") -> isLegendary = trimmedLine.split(",").any { it.trim() in listOf("Legendary", "Paradox", "Mythical", "Ultra Beast") }

                    trimmedLine.startsWith("Generation =") -> {
                        generation = trimmedLine.substringAfter("Generation =").trim().toInt()
                        if (generation !in 1..9){
                            throw ParameterException("La generación $generation no existe.")
                        }
                    }

                }
            }

            // Agregamos el último Pokémon si existe
            if (name != null && pokedexDescription != null) {
                pokemonList.add(
                    Pokemon(
                        idPokemon = null,
                        nombre = name ?: "",
                        descripcion = pokedexDescription ?: "",
                        tipo1 = types[0],
                        tipo2 = types[1],
                        legendario = isLegendary,
                        generacion = generation,
                        habilidad = abilities[0],
                    )
                )
            }

            pokemonList.forEach {
                pokemonRepository.save(it)
            }
        }
        catch (e: NullPointerException){
            throw FileNotFoundException("src/main/resources/static/pokemon.txt")
        }
    }

    fun getPokemonById(id: Int): Pokemon? {
        return pokemonRepository.findByIdOrNull(id)
    }

    fun getAllPokemon(): List<Pokemon>?{
        return pokemonRepository.findAll()
    }

    fun getByType(tipo: String): List<Pokemon>?{

        when (tipo) {
            Tipos.FUEGO.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.FUEGO.original || it.tipo2 == Tipos.FUEGO.original }
            }
            Tipos.AGUA.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.AGUA.original || it.tipo2 == Tipos.AGUA.original }
            }
            Tipos.PLANTA.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.PLANTA.original || it.tipo2 == Tipos.PLANTA.original }
            }
            Tipos.ELECTRICO.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.ELECTRICO.original || it.tipo2 == Tipos.ELECTRICO.original }
            }
            Tipos.TIERRA.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.TIERRA.original || it.tipo2 == Tipos.TIERRA.original }
            }
            Tipos.ROCA.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.ROCA.original || it.tipo2 == Tipos.ROCA.original }
            }
            Tipos.ACERO.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.ACERO.original || it.tipo2 == Tipos.ACERO.original }
            }
            Tipos.HADA.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.HADA.original || it.tipo2 == Tipos.HADA.original }
            }
            Tipos.SINIESTRO.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.SINIESTRO.original || it.tipo2 == Tipos.SINIESTRO.original }
            }
            Tipos.PSIQUICO.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.PSIQUICO.original || it.tipo2 == Tipos.PSIQUICO.original }
            }
            Tipos.FANTASMA.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.FANTASMA.original || it.tipo2 == Tipos.FANTASMA.original }
            }
            Tipos.DRAGON.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.DRAGON.original || it.tipo2 == Tipos.DRAGON.original }
            }
            Tipos.HIELO.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.HADA.original || it.tipo2 == Tipos.HIELO.original }
            }
            Tipos.BICHO.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.BICHO.original || it.tipo2 == Tipos.BICHO.original }
            }
            Tipos.NORMAL.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.NORMAL.original || it.tipo2 == Tipos.NORMAL.original }
            }
            Tipos.VOLADOR.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.VOLADOR.original || it.tipo2 == Tipos.VOLADOR.original }
            }
            Tipos.VENENO.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.VENENO.original || it.tipo2 == Tipos.VENENO.original }
            }
            Tipos.LUCHA.spanish -> {
                return pokemonRepository.findAll().filter { it.tipo1 == Tipos.LUCHA.original || it.tipo2 == Tipos.LUCHA.original }
            }
            else -> {
                throw ParameterException("El tipo: $tipo no existe o no es válido.")
            }
        }
    }

    fun getByGen(generacion: Int): List<Pokemon> {
        if (generacion in 1..9){
            return pokemonRepository.findAll().filter { it.generacion == generacion }
        }
        else{
            throw ParameterException("La generacion: $generacion no existe.")
        }
    }

    fun updatePokemon(id: Int, pokemon: Pokemon): Pokemon{
        val existingPokemon = pokemonRepository.findById(id)

        existingPokemon.ifPresent {
            it.descripcion = pokemon.descripcion
            it.tipo1 = pokemon.tipo1
            it.tipo2 = pokemon.tipo2
            it.habilidad = pokemon.habilidad
            it.legendario = it.legendario
            it.generacion = pokemon.generacion
        }
        pokemonRepository.save(existingPokemon.get())
        if (existingPokemon.isEmpty){
            throw ParameterException("No se ha encontrado un pokemon con el id: $id")
        }
        return existingPokemon.get()
    }

    fun deletePokemon(id: Int): Pokemon{
        val pokemon = getPokemonById(id)
        if (pokemon == null){
            throw NotFoundException("No se ha encontrado ningún pokemon con el id: $id")
        }
        else{
            pokemonRepository.delete(pokemon)
            return pokemon
        }
    }

}