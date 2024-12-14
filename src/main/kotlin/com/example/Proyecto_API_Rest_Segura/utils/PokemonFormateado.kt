package com.example.Proyecto_API_Rest_Segura.utils

import com.example.Proyecto_API_Rest_Segura.model.Movimiento
import jakarta.persistence.*

class PokemonFormateado(

    val idPokemon: Int?,

    val nombre: String,

    var descripcion: String,

    var tipo1: String,

    var tipo2: String?,

    val movimientos: List<Movimiento> = mutableListOf(),

    var habilidad: String,

    var legendario: Boolean,

    var generacion: Int
) {
}