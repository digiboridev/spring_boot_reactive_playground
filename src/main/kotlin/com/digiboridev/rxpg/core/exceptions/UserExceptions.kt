package com.digiboridev.rxpg.core.exceptions

import org.springframework.http.HttpStatus


sealed class UserExceptions(code: HttpStatus, message: String) : BaseException(code, message) {

    companion object Factory {
        fun notFound() = NotFound()
    }
}


class NotFound() : UserExceptions(HttpStatus.NOT_FOUND, "User not found")