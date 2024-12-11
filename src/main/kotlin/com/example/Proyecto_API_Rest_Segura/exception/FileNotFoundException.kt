package com.example.Proyecto_API_Rest_Segura.exception

class FileNotFoundException(mensaje: String) : Exception("No se ha podido encontrar el archivo para poblar la base de datos. Archivo: $mensaje") {
}