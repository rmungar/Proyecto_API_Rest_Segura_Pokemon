package com.example.Proyecto_API_Rest_Segura.repository

import com.example.Proyecto_API_Rest_Segura.model.Movimiento
import org.springframework.data.jpa.repository.JpaRepository

interface MovimientoRepository: JpaRepository<Movimiento, String> {
}