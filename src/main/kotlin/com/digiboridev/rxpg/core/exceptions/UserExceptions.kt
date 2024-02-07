package com.digiboridev.rxpg.core.exceptions

import org.springframework.http.HttpStatus


sealed class UserExceptions{
    class NotFound() : BaseException(HttpStatus.NOT_FOUND, "User not found")
}
