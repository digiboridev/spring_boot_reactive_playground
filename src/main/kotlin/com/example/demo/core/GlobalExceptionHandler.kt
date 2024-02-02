package com.example.demo.core

import com.example.demo.core.exceptions.BaseException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException): ResponseEntity<Any> {
        val body = mapOf(
            "timestamp" to Instant.now(),
            "status" to ex.code.value(),
            "message" to "EH: " +  ex.message
        )
        return ResponseEntity(body, ex.code)
    }

    @ExceptionHandler(Exception::class)
    fun handleAnException(ex: Exception): ResponseEntity<Any> {
        val body = mapOf(
            "timestamp" to Instant.now(),
            "status" to 500,
            "message" to "EH: " + ex.message
        )
        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(e: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors = e.bindingResult.fieldErrors.map { it.defaultMessage }
        val body = mapOf(
            "timestamp" to Instant.now(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "message" to "Invalid request",
            "errors" to errors,
        )
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

}

