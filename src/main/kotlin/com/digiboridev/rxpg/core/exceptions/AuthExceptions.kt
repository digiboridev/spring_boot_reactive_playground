package com.digiboridev.rxpg.core.exceptions

import org.springframework.http.HttpStatus


sealed class AuthExceptions(code: HttpStatus, message: String) : BaseException(code, message) {

    companion object Factory {
        fun emailAlreadyTaken() = EmailAlreadyTaken()
        fun invalidCredentials(name: String) = InvalidCredentials(name)
    }
}

class EmailAlreadyTaken() : AuthExceptions(HttpStatus.CONFLICT, "Email already taken")
class InvalidCredentials(name: String) : AuthExceptions(HttpStatus.BAD_REQUEST, "Invalid $name")
