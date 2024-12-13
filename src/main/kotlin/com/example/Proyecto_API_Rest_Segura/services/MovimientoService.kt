package com.example.Proyecto_API_Rest_Segura.services

import com.example.Proyecto_API_Rest_Segura.exception.FileNotFoundException
import com.example.Proyecto_API_Rest_Segura.exception.NotFoundException
import com.example.Proyecto_API_Rest_Segura.exception.ParameterException
import com.example.Proyecto_API_Rest_Segura.model.Movimiento
import com.example.Proyecto_API_Rest_Segura.repository.MovimientoRepository
import com.example.Proyecto_API_Rest_Segura.utils.Tipos
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.File

@Service
class MovimientoService {

    @Autowired
    private lateinit var movimientoRepository: MovimientoRepository


    fun populateMovimientosTable(){
        try {
            val movimientoFilePath = "src/main/resources/static/moves.txt"
            val moveList = mutableListOf<Movimiento>()
            val file = File(movimientoFilePath)

            var name: String? = null
            var description: String? = null
            var type: String? = null
            var category: String? = null
            var power: Int? = null
            var accuracy: Int? = null
            var totalPP: Int = 0

            file.forEachLine { line ->
                val trimmedLine = line.trim()

                when {
                    trimmedLine.startsWith("[") && trimmedLine.endsWith("]") -> {
                        // Cuando encontramos una nueva entrada, almacenamos la anterior (si existe)
                        if (name != null && description != null && type != null && category != null) {
                            moveList.add(
                                Movimiento(
                                    nombreMovimiento = name ?: "",
                                    descripcion = description ?: "",
                                    tipo = type ?: "",
                                    categoria = category ?: "",
                                    potencia = power ?: 0,
                                    precision = accuracy ?: 0,
                                    usos = totalPP
                                )
                            )
                        }

                        // Reiniciamos los valores para el siguiente movimiento
                        name = trimmedLine.removeSurrounding("[", "]")
                        description = null
                        type = null
                        category = null
                        power = null
                        accuracy = null
                        totalPP = 0
                    }

                    trimmedLine.startsWith("Name =") -> name = trimmedLine.substringAfter("Name =").trim()

                    trimmedLine.startsWith("Description =") -> description = trimmedLine.substringAfter("Description =").trim()

                    trimmedLine.startsWith("Type =") -> type = trimmedLine.substringAfter("Type =").trim()

                    trimmedLine.startsWith("Category =") -> category = trimmedLine.substringAfter("Category =").trim()

                    trimmedLine.startsWith("Power =") -> power = trimmedLine.substringAfter("Power =").trim().toIntOrNull()

                    trimmedLine.startsWith("Accuracy =") -> accuracy = trimmedLine.substringAfter("Accuracy =").trim().toIntOrNull()

                    trimmedLine.startsWith("TotalPP =") -> totalPP = trimmedLine.substringAfter("TotalPP =").trim().toInt()
                }
            }

            // Agregamos el último movimiento si existe
            if (name != null && description != null && type != null && category != null) {
                moveList.add(
                    Movimiento(
                        nombreMovimiento = name ?: "",
                        descripcion = description ?: "",
                        tipo = type ?: "",
                        categoria = category ?: "",
                        potencia = power ?: 0,
                        precision = accuracy ?: 0,
                        usos = totalPP
                    )
                )
            }
            moveList.forEach {
                movimientoRepository.save(it)
            }
        }
        catch (e: NullPointerException) {
            throw FileNotFoundException("src/main/resources/static/moves.txt")
        }

    }

    fun getMovimiento(id: String): Movimiento?{
        return movimientoRepository.findByIdOrNull(id)
    }

    fun getAllMovimientos(): MutableList<Movimiento>?{
        return movimientoRepository.findAll()
    }

    fun getMovimientosByCategoria(categoria: String): List<Movimiento>{
        val listaMovimientos = movimientoRepository.findAll()
        when (categoria.uppercase()){
            "FISICO" -> {
                val listaFiltrada = listaMovimientos.filter { it.categoria == "Physical" }
                return listaFiltrada
            }
            "ESPECIAL" -> {
                val listaFiltrada = listaMovimientos.filter { it.categoria == "Special" }
                return listaFiltrada
            }
            "ESTADO" -> {
                val listaFiltrada = listaMovimientos.filter { it.categoria == "Status" }
                return listaFiltrada
            }
            else -> {
                throw ParameterException("La categoría: $categoria no existe o no es válida.")
            }
        }
    }

    fun getMovimientosByTipo(tipo: String): List<Movimiento>{
        var validType = false
        var formattedType = ""
        for (type in Tipos.entries){
            if (type.spanish == tipo){
                validType = true
                formattedType = type.original
            }
        }
        if (validType){
            return movimientoRepository.findAll().filter { it.tipo == formattedType }
        }
        else{
            throw ParameterException("El tipo: $tipo no existe o no es válido.")
        }

    }

    fun filtradoPorCategoriaYTipo(categoria: String, tipo: String): List<Movimiento>{

        val movimientosPorTipos = getMovimientosByTipo(tipo)
        val movimientosPorCategoria = getMovimientosByCategoria(categoria)
        val listaFinal = mutableListOf<Movimiento>()
        if (movimientosPorTipos.isNotEmpty() && movimientosPorCategoria.isNotEmpty()){
            for (movimiento in movimientosPorTipos){
                if (movimientosPorCategoria.contains(movimiento)){
                    listaFinal.add(movimiento)
                }
            }
            return listaFinal
        }
        else{
            if (categoria.isNotEmpty()){
                throw ParameterException("La categoria: $categoria no existe o no es válida.")
            }
            throw ParameterException("El tipo: $tipo no existe o no es válido.")
        }

    }

    fun updateMovimiento(id: String, movimiento: Movimiento): Movimiento? {
        val movimientoExistente = getMovimiento(id)

        if (movimientoExistente != null){
            movimientoExistente.tipo = movimiento.tipo
            movimientoExistente.categoria = movimiento.categoria
            movimientoExistente.descripcion = movimiento.descripcion
            movimientoExistente.potencia = movimiento.potencia
            movimientoExistente.precision = movimiento.precision
            movimientoExistente.usos = movimiento.usos
        }
        else{
            throw ParameterException("No existe ningun movimiento con el id $id")
        }
        movimientoRepository.save(movimientoExistente)
        return movimientoExistente
    }

    fun deleteMovimiento(id: String){

        val movimiento = getMovimiento(id)
        if (movimiento != null){
            movimientoRepository.delete(movimiento)
        }
        else{
            throw NotFoundException("No existe ningún movimiento con el id $id")
        }

    }
}