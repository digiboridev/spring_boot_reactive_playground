package com.digiboridev.rxpg.core

import java.time.Instant

data class ErrorResponse(
    val code: Int,
    val message: String,
    val errors: List<String>,
    val timestamp: Instant = Instant.now()
)