package com.example.demo
import org.springframework.data.annotation.Id;


data class Person(
    @Id
    val id: String,
    val name: String,
    val age: Int
)
