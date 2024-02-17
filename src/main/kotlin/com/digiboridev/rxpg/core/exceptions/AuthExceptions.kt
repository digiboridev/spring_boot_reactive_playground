package com.digiboridev.rxpg.core.exceptions

import org.springframework.http.HttpStatus


sealed class AuthException(code: HttpStatus, message: String) : BaseException(code, message) {

    companion object Factory {
        fun emailAlreadyTaken() = EmailAlreadyTaken()
        fun invalidCredentials(fieldName: String) = InvalidCredentials(fieldName)
    }
}

class EmailAlreadyTaken() : AuthException(HttpStatus.CONFLICT, "Email already taken")
class InvalidCredentials(fieldName: String) : AuthException(HttpStatus.BAD_REQUEST, "Invalid $fieldName")
