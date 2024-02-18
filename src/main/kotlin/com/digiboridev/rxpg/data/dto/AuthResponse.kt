package com.digiboridev.rxpg.data.dto

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)