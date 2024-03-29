package com.digiboridev.rxpg.data.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "brands")
data class Brand(
    @Id
    val id: String = ObjectId().toString(),

    @Indexed(unique = true)
    @TextIndexed
    val name: String,
    @TextIndexed
    val description: String,

    val vec : List<Double>? = null,

    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val updatedAt: Instant = Instant.now(),
    @Version
    val version: Long = 0
)


