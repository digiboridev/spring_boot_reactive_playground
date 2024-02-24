package com.digiboridev.rxpg.data.dto

import java.time.Instant

data class BrandData(
    val id: String,
    val name: String,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)