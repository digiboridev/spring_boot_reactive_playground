package com.example.demo.persons
import org.springframework.data.mongodb.repository.MongoRepository

interface PersonsRepository : MongoRepository<Person, String> {
    fun findByName(name: String): List<Person>
    fun findByAge(age: Int): List<Person>
}


