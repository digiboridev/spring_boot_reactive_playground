package com.example.demo
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*


@Document( collection = "persons")
data class Person(
    @Id
    val id: String = ObjectId().toString(),
    @Indexed(unique = true)
    val name: String,
    @Field("gg")
    val age: Int,
    @Indexed
    val address: String? = null,

    @CreatedDate
    val createdBy: Date =  Date(),
    @LastModifiedDate
    val lastModifiedBy: Date = Date(),
    @Version
    val version: Long = 1
)
