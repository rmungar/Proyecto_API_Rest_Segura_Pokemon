package com.example.Proyecto_API_Rest_Segura.controller

import com.example.Proyecto_API_Rest_Segura.services.MovimientoService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/movimientos")
class MovimientoController {

    private lateinit var movimientoService: MovimientoService

    @PostMapping("/")
    fun RepoblateDataBase(){
        movimientoService.populateMovimientosTable()
    }

}