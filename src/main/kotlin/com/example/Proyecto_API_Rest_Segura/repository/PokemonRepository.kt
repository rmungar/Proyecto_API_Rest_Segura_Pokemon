package com.example.Proyecto_API_Rest_Segura.repository

import com.example.Proyecto_API_Rest_Segura.model.Pokemon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PokemonRepository: JpaRepository<Pokemon, Int> {



}