package com.example.Proyecto_API_Rest_Segura.model

import jakarta.persistence.*


@Entity
@Table(name = "movimientos")
class Movimiento(
    @Id
    val nombreMovimiento: String,
    @Column(nullable = false, length = 1000)
    val descripcion: String,
    @Column(nullable = false)
    val tipo: String,
    @Column(nullable = false)
    val categoria: String,
    @Column(nullable = false)
    val potencia: Int,
    @Column(nullable = false)
    val precision: Int,
    @Column(nullable = false)
    val usos: Int
) {
    override fun toString(): String {
        return "$nombreMovimiento ; $descripcion; $tipo ; $categoria ; $potencia ; $precision ; $usos"
    }
}