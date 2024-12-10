package com.example.Proyecto_API_Rest_Segura.services

import com.example.Proyecto_API_Rest_Segura.model.Pokemon
import com.example.Proyecto_API_Rest_Segura.repository.PokemonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File

@Service
class PokemonService {
    @Autowired
    private lateinit var pokemonRepository: PokemonRepository


    fun poblatePokemonTable(){
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
                                    habilidad = abilities[0]
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
                                    habilidad = abilities[0]
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

                trimmedLine.startsWith("Generation =") -> generation = trimmedLine.substringAfter("Generation =").trim().toInt()
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
                    habilidad = abilities[0]
                )
            )
        }

        pokemonList.forEach {
            pokemonRepository.save(it)
        }
    }


}