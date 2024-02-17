package com.digiboridev.rxpg.core.exceptions

import org.springframework.http.HttpStatus

open class BaseException(val code: HttpStatus, override val message: String) : RuntimeException(message)

