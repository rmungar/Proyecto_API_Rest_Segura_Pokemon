package com.example.Proyecto_API_Rest_Segura.model

import jakarta.persistence.*

@Entity
@Table(name = "usuarios")
class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,
    @Column(name = "userName", nullable = false, length = 20)
    var username: String? = null,
    @Column(name = "password", nullable = false)
    var password: String? = null,
    @Column(name = "role", nullable = false)
    var roles: String? = null
)