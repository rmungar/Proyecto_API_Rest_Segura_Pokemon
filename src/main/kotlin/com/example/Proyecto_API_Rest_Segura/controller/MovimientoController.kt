package com.example.Proyecto_API_Rest_Segura.controller

import com.example.Proyecto_API_Rest_Segura.services.MovimientoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/movimientos")
class MovimientoController {

    @Autowired
    private lateinit var movimientoService: MovimientoService

    @PostMapping("/")
    fun RepoblateDataBase(): ResponseEntity<Any?> {
        try{
            movimientoService.populateMovimientosTable()
            return ResponseEntity<Any?>(HttpStatus.OK)
        }
        catch(e:Exception){
            return ResponseEntity<Any?>(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    @GetMapping("/{id}")
    fun getMovimientoByName(
        @PathVariable("id") id: String?
    ): ResponseEntity<Any?>{
        try {
            if (id == null || id.isEmpty()){
                return ResponseEntity<Any?>(HttpStatus.BAD_REQUEST)
            }
            else{
                val movimiento = movimientoService.getMovimiento(id)
                return ResponseEntity<Any?>(movimiento, HttpStatus.OK)
            }
        }
        catch (e:Exception){
            return ResponseEntity<Any?>(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/")
    fun getAllMovimientos(): ResponseEntity<Any?> {
        try {
            val listaMovimientos = movimientoService.getAllMovimientos()
            return ResponseEntity<Any?>(listaMovimientos, HttpStatus.OK)
        }
        catch (e:Exception){
            return ResponseEntity<Any?>(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping()
    fun getMovimientosByCategoria(){

    }


}