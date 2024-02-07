package com.digiboridev.rxpg.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

enum class Role {
    ADMIN, MANAGER, CUSTOMER
}

@Document(collection = "users")
data class User(
    @Id
    val id: String = ObjectId().toString(),

    @TextIndexed
    val firstName: String = "",
    @TextIndexed
    val lastName: String = "",
    val role: Role = Role.CUSTOMER,
    @Indexed(unique = true)
    val email: String,
    val emailVerified : Boolean = false,
    val password: String,

    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val updatedAt: Instant = Instant.now(),
    @Version
    val version: Long = 0
)
