package com.example.Proyecto_API_Rest_Segura.services

import com.example.Proyecto_API_Rest_Segura.model.Movimiento
import com.example.Proyecto_API_Rest_Segura.repository.MovimientoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File

@Service
class MovimientoService {

    @Autowired
    private lateinit var movimientoRepository: MovimientoRepository


    fun populateMovimientosTable(){
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
                                nombre = name ?: "",
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
                    nombre = name ?: "",
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


}