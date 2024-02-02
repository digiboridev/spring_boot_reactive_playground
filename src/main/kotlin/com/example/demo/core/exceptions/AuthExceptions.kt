package com.example.demo.core.exceptions

import org.springframework.http.HttpStatus


sealed class AuthExceptions {
    class EmailAlreadyTaken() : BaseException(HttpStatus.CONFLICT, "Email already taken")
    class InvalidCredentials(name: String) : BaseException(HttpStatus.BAD_REQUEST, "Invalid $name")
}