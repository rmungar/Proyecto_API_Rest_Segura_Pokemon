package com.example.Proyecto_API_Rest_Segura.model

import jakarta.persistence.*


@Entity
@Table(name = "movimientos")
class Movimiento(
    @Id
    val nombreMovimiento: String,
    @Column(nullable = false, length = 1000)
    var descripcion: String,
    @Column(nullable = false)
    var tipo: String,
    @Column(nullable = false)
    var categoria: String,
    @Column(nullable = false)
    var potencia: Int,
    @Column(nullable = false)
    var precision: Int,
    @Column(nullable = false)
    var usos: Int
) {
    override fun toString(): String {
        return "$nombreMovimiento ; $descripcion; $tipo ; $categoria ; $potencia ; $precision ; $usos"
    }
}