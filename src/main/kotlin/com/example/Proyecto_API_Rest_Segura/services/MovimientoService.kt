package com.example.Proyecto_API_Rest_Segura.services

import com.example.Proyecto_API_Rest_Segura.exception.AlreadyFullException
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
        if (movimientoRepository.count() > 0) {
            throw AlreadyFullException("Movimientos")
        }
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
                        name = trimmedLine.removeSurrounding("[", "]")  // Aquí guardamos solo el nombre dentro de los corchetes
                        description = null
                        type = null
                        category = null
                        power = null
                        accuracy = null
                        totalPP = 0
                    }

                    trimmedLine.startsWith("Description =") -> description = trimmedLine.substringAfter("Description =").trim()

                    trimmedLine.startsWith("Type =") -> type = trimmedLine.substringAfter("Type =").trim()

                    trimmedLine.startsWith("Category =") -> category = trimmedLine.substringAfter("Category =").trim()

                    trimmedLine.startsWith("Power =") -> {
                        power = trimmedLine.substringAfter("Power =").trim().toIntOrNull()
                        if (power!! < 0) {
                            throw ParameterException("El valor del poder del movimiento no puede ser inferior a 0")
                        }
                        if (power!! > 999) {
                            throw ParameterException("El valor del poder del movimiento no puede ser superior a 999")
                        }
                    }

                    trimmedLine.startsWith("Accuracy =") -> {
                        accuracy = trimmedLine.substringAfter("Accuracy =").trim().toIntOrNull()
                        if (accuracy!! < 0) {
                            throw ParameterException("El valor de la precisión del movimiento $name no puede ser inferior a 0")
                        }
                        if (accuracy!! > 100) {
                            throw ParameterException("El valor de la precisión del movimiento $name no puede ser superior a 100")
                        }
                    }

                    trimmedLine.startsWith("TotalPP =") -> {
                        totalPP = trimmedLine.substringAfter("TotalPP =").trim().toInt()
                        if (totalPP < 0) {
                            throw ParameterException("El valor de los usos del movimiento $name no pueden ser inferiores a 0")
                        }
                        if (totalPP > 99) {
                            throw ParameterException("El valor de los usos del movimiento $name no pueden ser superiores a 99")
                        }
                    }
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
                addMovimiento(it)
            }
        }
        catch (e: NullPointerException) {
            throw FileNotFoundException("src/main/resources/static/moves.txt")
        }


    }

    fun addMovimiento(movimiento: Movimiento){
        val movimientoExistente = movimientoRepository.findByIdOrNull(movimiento.nombreMovimiento)
        if (movimientoExistente != null) {
            throw ParameterException("Ya existe un movimiento con el nombre ${movimiento.nombreMovimiento}.")
        }
        else{
            if (movimiento.nombreMovimiento == ""){
                throw ParameterException("El nombre del movimiento no puede estar vacío.")
            }
            if (movimiento.usos !in 0..99){
                throw ParameterException("Los usos ${movimiento.usos} no se encuentran en el rango 1..99")
            }
            if (movimiento.categoria == "") {
                throw ParameterException("La catégoria del movimiento no puede estar vacía.")
            }
            else{
                if (movimiento.categoria.uppercase() != "FISICO" && movimiento.categoria.uppercase() != "ESPECIAL" && movimiento.categoria.uppercase() != "ESTADO" && movimiento.categoria.uppercase() != "PHYSICAL" && movimiento.categoria.uppercase() != "SPECIAL" && movimiento.categoria.uppercase() != "STATUS") {
                    throw ParameterException("La categoría del movimiento ha de ser FISICO, ESPECIAL O ESTADO.")
                }
                else{
                    when (movimiento.categoria.uppercase()){
                        "FISICO" -> {
                            movimiento.categoria = "Physical"
                        }
                        "ESPECIAL" -> {
                            movimiento.categoria = "Special"
                        }
                        "ESTADO" -> {
                            movimiento.categoria = "Status"
                        }
                        else -> {
                            movimiento.categoria = movimiento.categoria.uppercase()
                        }
                    }

                }
            }
            if (movimiento.potencia !in 0..999){
                throw ParameterException("La potencia ${movimiento.potencia} no se encuentra en el rango 0..999.")
            }
            if (movimiento.precision !in 0..100){
                throw ParameterException("La precision ${movimiento.precision} no se encuentra en el rango 0..100.")
            }
            var valid = false
            for (entry in Tipos.entries) {
                if (movimiento.tipo.uppercase() == entry.spanish || movimiento.tipo.uppercase() == entry.original){
                    movimiento.tipo = entry.original
                    valid = true
                }
            }
            if (!valid){
                throw ParameterException("El tipo ${movimiento.tipo} no existe.")
            }
            movimientoRepository.save(movimiento)
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
            if (type.spanish == tipo.uppercase()){
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
            if (categoria.isEmpty()){
                throw ParameterException("La categoria: $categoria no existe o no es válida.")
            }else{
                throw ParameterException("El tipo: $tipo no existe o no es válido.")
            }
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

        val movimiento = getMovimiento(id.uppercase())
        if (movimiento != null){
            movimientoRepository.delete(movimiento)
        }
        else{
            throw NotFoundException("No existe ningún movimiento con el id $id")
        }

    }
}