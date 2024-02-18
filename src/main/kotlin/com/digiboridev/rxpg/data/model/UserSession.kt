package com.digiboridev.rxpg.data.model

import com.digiboridev.rxpg.data.valueObject.Role
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant


@Document(collection = "user_sessions")
data class UserSession(
    @Id
    val id: String = ObjectId().toString(),
    val userId: String,

    val expiresAt: Instant,

    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @Version
    val version: Long = 0
)
