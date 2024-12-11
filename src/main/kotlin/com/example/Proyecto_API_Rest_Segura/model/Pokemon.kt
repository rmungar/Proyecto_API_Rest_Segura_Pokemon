package com.example.Proyecto_API_Rest_Segura.model

import jakarta.persistence.*


@Entity
@Table(name = "pokemon")
class Pokemon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idPokemon: Int?,
    @Column(nullable = false)
    val nombre: String,
    @Column(nullable = false ,length = 1000)
    var descripcion: String,
    @Column(nullable = false)
    var tipo1: String,
    @Column
    var tipo2: String?,
    @Column(nullable = false)
    var habilidad: String,
    @Column(nullable = false)
    val legendario: Boolean,
    @Column(nullable = false)
    val generacion: Int
) {
    override fun toString(): String {
        return "$idPokemon ; $nombre ; $tipo1 ; $tipo2 ; $descripcion ; $legendario ; $generacion"
    }
}