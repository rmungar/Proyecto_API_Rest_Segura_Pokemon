package com.example.Proyecto_API_Rest_Segura.exception

class NotFoundException(mensaje: String): Exception("Error en la validación (404). $mensaje") {
}