package com.example.Proyecto_API_Rest_Segura.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class APIExceptionHandler {


    @ExceptionHandler(
        IllegalArgumentException::class,
        NumberFormatException::class,
        ParameterException::class,
        AuthenticationException::class,
        AlreadyFullException::class,
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleBadRequest(request: HttpServletRequest, e:Exception): Error {
        return Error(e.message!!, request.requestURI)
    }


    @ExceptionHandler(
        NotFoundException::class,
        FileNotFoundException::class,
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleNotFound(request: HttpServletRequest, e:Exception): Error {
        return Error(e.message!!, request.requestURI)
    }


}