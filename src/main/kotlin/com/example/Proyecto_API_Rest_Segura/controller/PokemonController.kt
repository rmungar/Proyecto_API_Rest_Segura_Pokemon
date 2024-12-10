package com.example.Proyecto_API_Rest_Segura.controller

import com.example.Proyecto_API_Rest_Segura.model.Pokemon
import com.example.Proyecto_API_Rest_Segura.services.PokemonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/pokemon")
class PokemonController {

    @Autowired
    private lateinit var pokemonService : PokemonService



    @PostMapping("/")
    fun RepoblateDataBase(): ResponseEntity<Any?>{
        try{
            pokemonService.poblatePokemonTable()
            return ResponseEntity(HttpStatus.OK)
        }
        catch (e: Exception){
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }


    @GetMapping("/{id}")
    fun GetPokemonById(
        @PathVariable("id") id: Int?
    ): ResponseEntity<Any?>{
        try {
            if (id != null){
                val pokemon = pokemonService.getPokemonById(id)
                return ResponseEntity(pokemon, HttpStatus.OK)
            }
            else{
                return ResponseEntity(HttpStatus.BAD_REQUEST)
            }
        }catch (e: Exception){
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/")
    fun getAllPokemon(): ResponseEntity<Any?>{
        try {
            val listaPokemon = pokemonService.getAllPokemon()
            return ResponseEntity(listaPokemon, HttpStatus.OK)
        }
        catch (e: Exception){
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }



}