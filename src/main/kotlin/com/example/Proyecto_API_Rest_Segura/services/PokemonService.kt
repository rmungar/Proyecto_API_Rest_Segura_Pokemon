package com.example.Proyecto_API_Rest_Segura.services

import com.example.Proyecto_API_Rest_Segura.exception.AlreadyFullException
import com.example.Proyecto_API_Rest_Segura.exception.FileNotFoundException
import com.example.Proyecto_API_Rest_Segura.exception.NotFoundException
import com.example.Proyecto_API_Rest_Segura.exception.ParameterException
import com.example.Proyecto_API_Rest_Segura.model.Movimiento
import com.example.Proyecto_API_Rest_Segura.model.Pokemon
import com.example.Proyecto_API_Rest_Segura.repository.PokemonRepository
import com.example.Proyecto_API_Rest_Segura.utils.PokemonFormateado
import com.example.Proyecto_API_Rest_Segura.utils.Tipos
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.File

@Service
class PokemonService {

    @Autowired
    private lateinit var pokemonRepository: PokemonRepository

    @Autowired
    private lateinit var movimientoService: MovimientoService

    fun poblatePokemonTable(){
        if (pokemonRepository.count() > 0) {
            throw AlreadyFullException("Pokémon")
        }
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
            var lastMoves: List<String> = emptyList()

            file.forEachLine { line ->
                val trimmedLine = line.trim()

                when {
                    trimmedLine.startsWith("[") && trimmedLine.endsWith("]") -> {
                        // Guardar el Pokémon anterior si existe
                        if (name != null && pokedexDescription != null) {

                            val pokemon = if (types.size > 1) {
                                Pokemon(
                                    idPokemon = null,
                                    nombre = name ?: "",
                                    descripcion = pokedexDescription ?: "",
                                    tipo1 = types[0],
                                    tipo2 = types.getOrNull(1),
                                    movimientos = lastMoves,
                                    habilidad = abilities.getOrElse(0) { "" },
                                    legendario = isLegendary,
                                    generacion = generation


                                )
                            } else {
                                Pokemon(
                                    idPokemon = null,
                                    nombre = name ?: "",
                                    descripcion = pokedexDescription ?: "",
                                    tipo1 = types[0],
                                    tipo2 = null,
                                    movimientos = lastMoves,
                                    habilidad = abilities.getOrElse(0) { "" },
                                    legendario = isLegendary,
                                    generacion = generation
                                )
                            }

                            pokemonList.add(pokemon)
                        }

                        // Reiniciar valores para el siguiente Pokémon
                        name = trimmedLine.removeSurrounding("[", "]")
                        pokedexDescription = null
                        types = emptyList()
                        isLegendary = false
                        generation = 0
                        abilities = emptyList()
                        lastMoves = emptyList()
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

                    trimmedLine.startsWith("Moves=") -> {
                        val moves = trimmedLine.substringAfter("Moves=").split(",").chunked(2) { it[1] } // Obtenemos solo los nombres de los movimientos
                        lastMoves = moves.takeLast(4)
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
            throw FileNotFoundException("src/main/resources/static/pokemon.txt" +  e.stackTraceToString())
        }
    }

    fun addPokemon(pokemon: Pokemon){

        var pokemonConElMismoId = false
        pokemonRepository.findAll().forEach {
            if (it.nombre == pokemon.nombre){
                pokemonConElMismoId = true
            }
        }
        if (pokemonConElMismoId) {
            throw ParameterException("Ya existe un pokemon con el nombre ${pokemon.nombre}")
        }
        else{
            if (pokemon.nombre == ""){
                throw ParameterException("El nombre del pokémon no puede estar vacío.")
            }
            if (pokemon.descripcion == ""){
                throw ParameterException("La descripción del pokémon no puede estar vacía.")
            }
            if (pokemon.generacion !in 1..9){
                throw ParameterException("La generación ${pokemon.generacion} no existe.")
            }
            if (pokemon.habilidad == ""){
                throw ParameterException("La habilidad del pokémon no puede estar vacía.")
            }

            if (pokemon.tipo1 == ""){
                throw ParameterException("Un pokémon debe tener mínimo un tipo")
            }
            else {
                var valid = false
                for (entry in Tipos.entries) {
                    if (pokemon.tipo1.uppercase() == entry.spanish || pokemon.tipo1.uppercase() == entry.original){
                        pokemon.tipo1 = entry.original
                        valid = true
                    }
                }
                if (!valid){
                    throw ParameterException("El tipo ${pokemon.tipo1} no existe.")
                }
            }
            if (pokemon.tipo2 != null && pokemon.tipo2 != ""){
                var valid = false
                for (entry in Tipos.entries) {
                    if (pokemon.tipo2!!.uppercase() == entry.spanish || pokemon.tipo2!!.uppercase() == entry.original){
                        pokemon.tipo2 = entry.original
                        valid = true
                    }
                }
                if (!valid){
                    throw ParameterException("El tipo ${pokemon.tipo2} no existe.")
                }
            }
            else{
                pokemon.tipo2 = null
            }
            if (pokemon.movimientos.isEmpty() || pokemon.movimientos.size < 4){
                throw ParameterException("Un pokémon ha de tener 4 movimientos.")
            }
            else{
                for (movimiento in pokemon.movimientos){
                    val mov = movimientoService.getMovimiento(movimiento)
                    if (mov == null){
                        throw ParameterException("El movimiento $movimiento no existe, creelo primero.")
                    }
                    else{
                        if (mov.tipo != pokemon.tipo1.uppercase()){
                            throw ParameterException("El pokémon no puede aprender este movimiento: $movimiento.")
                        }
                        else if (pokemon.tipo2 != null && pokemon.tipo2 != "" && mov.tipo != pokemon.tipo2?.uppercase()){
                            throw ParameterException("El pokémon no puede aprender este movimiento: $movimiento.")
                        }
                    }
                }
            }
            pokemon.idPokemon = null
            pokemonRepository.save(pokemon)
        }



    }

    fun getPokemonById(id: Int): PokemonFormateado? {
        val pokemon = pokemonRepository.findByIdOrNull(id)
        if (pokemon != null){
            val listaMovimiento = mutableListOf<Movimiento>()
            for (movimiento in pokemon.movimientos){
                listaMovimiento.add(movimientoService.getMovimiento(movimiento)!!)
            }
            val pokemonFormateado = PokemonFormateado(
                idPokemon = pokemon.idPokemon,
                nombre = pokemon.nombre,
                descripcion = pokemon.descripcion,
                tipo1 = pokemon.tipo1,
                tipo2 = pokemon.tipo2,
                habilidad = pokemon.habilidad,
                legendario = pokemon.legendario,
                generacion = pokemon.generacion,
                movimientos = listaMovimiento
            )
            return pokemonFormateado
        }
        else{
            throw NotFoundException("No existe ese pokemon en la base de datos")
        }
    }

    fun getAllPokemon(): List<PokemonFormateado>?{
        val lista = pokemonRepository.findAll()
        if (lista.isNotEmpty()){
            val listaFormateada = mutableListOf<PokemonFormateado>()
            for (pokemon in lista){
                val listaMovimiento = mutableListOf<Movimiento>()
                for (movimiento in pokemon.movimientos){
                    listaMovimiento.add(movimientoService.getMovimiento(movimiento)!!)
                }
                val pokemonFormateado = PokemonFormateado(
                    idPokemon = pokemon.idPokemon,
                    nombre = pokemon.nombre,
                    descripcion = pokemon.descripcion,
                    tipo1 = pokemon.tipo1,
                    tipo2 = pokemon.tipo2,
                    habilidad = pokemon.habilidad,
                    legendario = pokemon.legendario,
                    generacion = pokemon.generacion,
                    movimientos = listaMovimiento
                )
                listaFormateada.add(pokemonFormateado)
            }
            return listaFormateada
        }
        else{
            throw NotFoundException("No hay pokemon en la base de datos")
        }
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

        if (pokemon.descripcion == ""){
            throw ParameterException("La descripción del pokémon no puede estar vacía.")
        }
        if (pokemon.generacion !in 1..9){
            throw ParameterException("La generación ${pokemon.generacion} no existe.")
        }
        if (pokemon.habilidad == ""){
            throw ParameterException("La habilidad del pokémon no puede estar vacía.")
        }

        if (pokemon.tipo1 == ""){
            throw ParameterException("Un pokémon debe tener mínimo un tipo")
        }
        else {
            var valid = false
            for (entry in Tipos.entries) {
                if (pokemon.tipo1.uppercase() == entry.spanish || pokemon.tipo1.uppercase() == entry.original){
                    pokemon.tipo1 = entry.original
                    valid = true
                }
            }
            if (!valid){
                throw ParameterException("El tipo ${pokemon.tipo1} no existe.")
            }
        }
        if (pokemon.tipo2 != null && pokemon.tipo2 != ""){
            var valid = false
            for (entry in Tipos.entries) {
                if (pokemon.tipo2!!.uppercase() == entry.spanish || pokemon.tipo2!!.uppercase() == entry.original){
                    pokemon.tipo2 = entry.original
                    valid = true
                }
            }
            if (!valid){
                throw ParameterException("El tipo ${pokemon.tipo2} no existe.")
            }
        }
        else{
            pokemon.tipo2 = null
        }
        if (pokemon.movimientos.isEmpty() || pokemon.movimientos.size < 4){
            throw ParameterException("Un pokémon ha de tener 4 movimientos.")
        }
        else{
            for (movimiento in pokemon.movimientos){
                val mov = movimientoService.getMovimiento(movimiento)
                if (mov == null){
                    throw ParameterException("El movimiento $movimiento no existe, creelo primero.")
                }
                else{
                    if (mov.tipo != pokemon.tipo1.uppercase()){
                        throw ParameterException("El pokémon no puede aprender este movimiento: $movimiento.")
                    }
                    else if (pokemon.tipo2 != null && pokemon.tipo2 != "" && mov.tipo != pokemon.tipo2?.uppercase()){
                        throw ParameterException("El pokémon no puede aprender este movimiento: $movimiento.")
                    }
                }
            }
        }


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
        val pokemon = pokemonRepository.findByIdOrNull(id)
        if (pokemon == null){
            throw NotFoundException("No se ha encontrado ningún pokemon con el id: $id")
        }
        else{
            pokemonRepository.delete(pokemon)
            return pokemon
        }
    }

}