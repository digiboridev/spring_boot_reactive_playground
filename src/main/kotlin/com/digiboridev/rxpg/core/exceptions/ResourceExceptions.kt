package com.digiboridev.rxpg.core.exceptions

import org.springframework.http.HttpStatus


sealed class ResourceException(code: HttpStatus, message: String) : BaseException(code, message) {

    companion object Factory {
        fun notFound(name: String) = NotFound(name)
        fun noContent(name: String) = NoContent(name)
        fun conflict(name: String) = Conflict(name)
        fun forbidden(name: String) = Forbidden(name)
    }
}

class NotFound(name: String) : ResourceException(HttpStatus.NOT_FOUND, "$name not found")
class NoContent(name: String) : ResourceException(HttpStatus.NO_CONTENT, "$name not found")
class Conflict(name: String) : ResourceException(HttpStatus.CONFLICT, "$name already exists")
class Forbidden(name: String) : ResourceException(HttpStatus.FORBIDDEN, "Cannot have access to $name")
