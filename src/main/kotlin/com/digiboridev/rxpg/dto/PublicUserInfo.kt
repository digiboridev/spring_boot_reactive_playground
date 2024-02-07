package com.digiboridev.rxpg.dto

import com.digiboridev.rxpg.model.User

data class PublicUserInfo(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String,
) {
    constructor(user: User) : this(
        user.id,
        user.firstName,
        user.lastName,
        user.email,
        user.role.name,
    )
}
