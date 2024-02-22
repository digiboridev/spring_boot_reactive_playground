package com.digiboridev.rxpg.data.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "categories")
data class Category(
    @Id
    val id: String = ObjectId().toString(),

    val name: String,
    val description: String,
    val image: String? = null,
    val parentId: String? = null,

    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val updatedAt: Instant = Instant.now(),
    @Version
    val version: Long = 0
) {
    val isRoot: Boolean get() = parentId == null
}