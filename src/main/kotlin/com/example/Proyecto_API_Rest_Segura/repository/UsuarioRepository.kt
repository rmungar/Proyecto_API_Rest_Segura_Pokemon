package com.example.Proyecto_API_Rest_Segura.repository

import com.example.Proyecto_API_Rest_Segura.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UsuarioRepository: JpaRepository<Usuario, Int> {
    fun findByUsername(userName: String): Optional<Usuario>
}