package com.example.demo
import org.springframework.data.mongodb.repository.MongoRepository

interface PersonRepository : MongoRepository<Person, String> {
    fun findByName(name: String): List<Person>
    fun findByAge(age: Int): List<Person>
}


