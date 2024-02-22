package com.digiboridev.rxpg.core.exceptions

import org.springframework.http.HttpStatus


// Base app exception class
// It is open so that it can be extended by other classes and modules
open class BaseException(val code: HttpStatus, override val message: String) : RuntimeException(message)



// Authentication related exceptions
sealed class AuthException(code: HttpStatus, message: String) : BaseException(code, message) {

    companion object Factory {
        fun emailAlreadyTaken() = EmailAlreadyTaken()
        fun invalidCredentials(fieldName: String) = InvalidCredentials(fieldName)
        fun invalidToken() = InvalidToken()
        fun expiredToken() = ExpiredToken()
        fun expiredSession() = ExpiredSession()
    }
}

class EmailAlreadyTaken() : AuthException(HttpStatus.CONFLICT, "Email already taken")
class InvalidCredentials(fieldName: String) : AuthException(HttpStatus.BAD_REQUEST, "Invalid $fieldName")
class InvalidToken() : AuthException(HttpStatus.UNAUTHORIZED, "Invalid token")
class ExpiredToken() : AuthException(HttpStatus.UNAUTHORIZED, "Expired token")
class ExpiredSession() : AuthException(HttpStatus.UNAUTHORIZED, "Session expired")



// Resources related exceptions
// Name is used to specify the resource or action that caused the exception
sealed class ResourceException(code: HttpStatus, message: String) : BaseException(code, message) {

    companion object Factory {
        fun notFound(name: String) = NotFound(name)
        fun noContent(name: String) = NoContent(name)
        fun alreadyExist(name: String) = AlreadyExist(name)
        fun forbidden(name: String) = Forbidden(name)
    }
}

class NotFound(name: String) : ResourceException(HttpStatus.NOT_FOUND, "$name not found")
class NoContent(name: String) : ResourceException(HttpStatus.NO_CONTENT, "$name not found")
class AlreadyExist(name: String) : ResourceException(HttpStatus.CONFLICT, "$name already exists")
class Forbidden(name: String) : ResourceException(HttpStatus.FORBIDDEN, "Cannot have access to $name")
