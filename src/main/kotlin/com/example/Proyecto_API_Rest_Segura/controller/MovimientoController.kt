package com.example.Proyecto_API_Rest_Segura.controller

import com.example.Proyecto_API_Rest_Segura.exception.ParameterException
import com.example.Proyecto_API_Rest_Segura.model.Movimiento
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

    @PostMapping("/movimientos")
    fun addMovimiento(
        @RequestBody movimiento: Movimiento?
    ): ResponseEntity<Any?> {
        if(movimiento != null){
            movimientoService.addMovimiento(movimiento)
            return ResponseEntity(movimiento, HttpStatus.CREATED)
        }
        else{
            throw ParameterException("El parámetro movimiento no puede estar vacío")
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
                val movimiento = movimientoService.getMovimiento(id) ?: return ResponseEntity<Any?>(HttpStatus.NOT_FOUND)
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

    @GetMapping("/categoria/{categoria}")
    fun getMovimientosByCategoria(
        @PathVariable("categoria") categoria: String?
    ): ResponseEntity<Any?>{
        try{
            if (!categoria.isNullOrEmpty()){
                if (categoria.uppercase() == "FISICO" || categoria.uppercase() == "ESPECIAL" || categoria.uppercase() == "ESTADO"){
                    val listaMovimientos = movimientoService.getMovimientosByCategoria(categoria.uppercase())
                    return ResponseEntity<Any?>(listaMovimientos, HttpStatus.OK)
                }
                else {
                    throw ParameterException("La categoría $categoria no existe.")
                }
            }
            else{
                throw ParameterException("La categoría $categoria no puede estar vacia o ser nula.")
            }
        }
        catch (e:Exception){
            return ResponseEntity<Any?>(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    @GetMapping("/tipo/{tipo}")
    fun getMovimientosByTipo(
        @PathVariable("tipo") tipo: String?
    ): ResponseEntity<Any?>{
        try{
            if (!tipo.isNullOrEmpty()) {
                val listaMovimientos = movimientoService.getMovimientosByTipo(tipo.uppercase())
                return ResponseEntity<Any?>(listaMovimientos, HttpStatus.OK)
            }
            else{
                throw ParameterException("El tipo $tipo no existe.")
            }
        }
        catch (e:Exception){
            return ResponseEntity<Any?>(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    @GetMapping("/categoria/{categoria}/tipo/{tipo}")
    fun getMovimientosByCategoriaYTipo(
        @PathVariable("categoria") categoria: String?,
        @PathVariable("tipo") tipo: String?
    ): ResponseEntity<Any?>{

        if (!categoria.isNullOrEmpty() && !tipo.isNullOrEmpty()){
            val listaMovimientos = movimientoService.filtradoPorCategoriaYTipo(categoria, tipo)
            return ResponseEntity(listaMovimientos, HttpStatus.OK)
        }
        else{
            throw ParameterException("Los valores de categoría y tipo no pueden estar vacíos o ser nulos")
        }
    }

    @PutMapping("/{id}")
    fun updateMovimiento(
        @PathVariable("id") id: String?,
        @RequestBody movimiento: Movimiento?
    ): ResponseEntity<Any?> {
        if (id != null && movimiento != null) {
            val movimientoUpdateado = movimientoService.updateMovimiento(id, movimiento)
            return ResponseEntity(movimientoUpdateado, HttpStatus.OK)
        }
        else
            throw ParameterException("Ni el parametro id ni el cuerpo de la petición pueden estar vacíos")
    }

    @DeleteMapping("/{id}")
    fun deleteMovimiento(
        @PathVariable("id") id: String?,
    ): ResponseEntity<Any?>{
        if (id != null) {
            movimientoService.deleteMovimiento(id)
            return ResponseEntity(HttpStatus.OK)
        }else{
            throw ParameterException("El parámetro id no puede estar vacío")
        }
    }


}