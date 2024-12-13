package com.example.Proyecto_API_Rest_Segura.services

import com.example.Proyecto_API_Rest_Segura.exception.ParameterException
import com.example.Proyecto_API_Rest_Segura.model.Usuario
import com.example.Proyecto_API_Rest_Segura.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService: UserDetailsService {


    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository


    fun register(
        usuario: Usuario
    ): Usuario {

        if (usuario.username == null){
            throw ParameterException("El nombre no puede ser nulo.")
        }
        if (usuario.username!!.isEmpty() || usuario.username!!.isBlank()) {
            throw ParameterException("El nombre: ${usuario.username} no es válido.")
        }
        if (usuario.password == null){
            throw ParameterException("La contraseña no puede ser nula.")
        }
        if (usuario.password!!.isEmpty() || usuario.password!!.isBlank()) {
            throw ParameterException("La contraseña: ${usuario.password} no es válida.")
        }
        if (usuario.role == null){
            throw ParameterException("El parámetro rol no puede ser nulo")
        }
        if (usuario.role!!.uppercase() != "ADMIN" && usuario.role!!.uppercase() != "USER"){
            throw ParameterException("El rol: ${usuario.role} no es válido.")
        }
        usuario.role = usuario.role!!.uppercase()
        usuario.password = passwordEncoder.encode(usuario.password)
        usuarioRepository.save(usuario)
        return usuario
    }


    override fun loadUserByUsername(userName: String?): UserDetails {
        val usuario = usuarioRepository.findByUsername(userName!!).orElseThrow()

        val roles = usuario.role?.map { SimpleGrantedAuthority("ROLE_${it}") }
            ?.toList() ?: listOf()

        return User
            .builder()
            .username(usuario.username)
            .password(usuario.password)
            .authorities(roles)
            .build()
    }


    fun updateUser(usuario: Usuario): Usuario {
        val usuarioExistente = usuarioRepository.findByUsername(usuario.username!!).orElseThrow{ ParameterException("El usuario buscado no existe en la base de datos") }

        if (usuario.username == null){
            throw ParameterException("El nombre no puede ser nulo.")
        }
        if (usuario.username!!.isEmpty() || usuario.username!!.isBlank()) {
            throw ParameterException("El nombre: ${usuario.username} no es válido.")
        }
        if (usuario.password == null){
            throw ParameterException("La contraseña no puede ser nula.")
        }
        if (usuario.password!!.isEmpty() || usuario.password!!.isBlank()) {
            throw ParameterException("La contraseña: ${usuario.password} no es válida.")
        }
        if (usuario.role == null){
            throw ParameterException("El parámetro rol no puede ser nulo")
        }
        if (usuario.role!!.uppercase() != "ADMIN" && usuario.role!!.uppercase() != "USER"){
            throw ParameterException("El rol: ${usuario.role} no es válido.")
        }


        usuarioExistente.username = usuario.username
        usuarioExistente.password = passwordEncoder.encode(usuario.password)
        usuarioExistente.role = usuario.role
        usuarioRepository.save(usuarioExistente)
        return usuario
    }
}