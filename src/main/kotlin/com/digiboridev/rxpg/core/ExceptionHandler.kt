package com.digiboridev.rxpg.core

import com.digiboridev.rxpg.core.exceptions.BaseException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.support.WebExchangeBindException


@ControllerAdvice
class ExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleAnException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            errors = listOf(ex.message ?: "An unexpected error occurred")
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    fun handleBindException(ex: BindException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            code = HttpStatus.BAD_REQUEST.value(),
            message = HttpStatus.BAD_REQUEST.reasonPhrase,
            errors = ex.fieldErrors.map { it.defaultMessage ?: "An unexpected error occurred" }
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException::class)
    fun handleWebExchangeBindException(ex: WebExchangeBindException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            code = HttpStatus.BAD_REQUEST.value(),
            message = HttpStatus.BAD_REQUEST.reasonPhrase,
            errors = ex.fieldErrors.map { it.defaultMessage ?: "An unexpected error occurred" }
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            code = ex.code.value(),
            message = ex.code.reasonPhrase,
            errors = listOf(ex.message ?: "An unexpected error occurred")
        )
        return ResponseEntity(errorResponse, ex.code)
    }
}

