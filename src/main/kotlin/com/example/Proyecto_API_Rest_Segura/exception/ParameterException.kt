package com.example.Proyecto_API_Rest_Segura.exception

class ParameterException( mensaje: String ): Exception("El parametro pasado por la URI no es correcto (400); $mensaje") {
}