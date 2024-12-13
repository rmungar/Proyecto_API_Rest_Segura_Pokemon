package com.example.Proyecto_API_Rest_Segura.controller

import com.example.Proyecto_API_Rest_Segura.exception.ParameterException
import com.example.Proyecto_API_Rest_Segura.model.Usuario
import com.example.Proyecto_API_Rest_Segura.services.TokenService
import com.example.Proyecto_API_Rest_Segura.services.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var usuarioService: UsuarioService

    @Autowired
    private lateinit var tokenService: TokenService


    @PostMapping("/register")
    fun register(
        @RequestBody usuario: Usuario?
    ): ResponseEntity<Any?> {

        if (usuario != null) {
            val usuarioRegistrado = usuarioService.register(usuario)
            return ResponseEntity(usuarioRegistrado, HttpStatus.CREATED)
        }
        throw ParameterException("El parámetro usuario no puede estar vacío.")

    }

    @GetMapping("/login")
    fun login(
        @RequestBody usuario: Usuario?
    ): ResponseEntity<Any?> {

        if (usuario != null) {
            val authentication: Authentication
            authentication =authenticationManager.authenticate(UsernamePasswordAuthenticationToken(usuario.username, usuario.password))
            val token = tokenService.generarToken(authentication)
            return ResponseEntity(mapOf("Token" to token), HttpStatus.OK)
        }
        throw ParameterException("El parámetro usuario no puede estar vacío.")
    }


    @PutMapping("/usuario")
    fun updateUser(
        @RequestBody usuario: Usuario?
    ): ResponseEntity<Any?> {
        if (usuario != null) {
            val usuarioUpdateado = usuarioService.updateUser(usuario)
            return ResponseEntity(usuarioUpdateado, HttpStatus.OK)
        }
        throw ParameterException("El parámetro usuario no puede estar vacío.")
    }

}