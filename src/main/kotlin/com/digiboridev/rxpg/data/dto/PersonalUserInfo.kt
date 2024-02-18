package com.digiboridev.rxpg.data.dto


import com.digiboridev.rxpg.data.valueObject.Role
import com.digiboridev.rxpg.data.model.User
import java.time.Instant


data class PersonalUserInfo(
    val id: String,
    val firstName: String,
    val lastName: String,
    val role: Role,
    val email: String,
    val emailVerified : Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    constructor(user: User) : this(
        user.id,
        user.firstName,
        user.lastName,
        user.role,
        user.email,
        user.emailVerified,
        user.createdAt,
        user.updatedAt,
    )
}

