package com.example.Proyecto_API_Rest_Segura.exception

class AlreadyFullException(mensaje: String): Exception("La tabla $mensaje ya está poblada") {
}