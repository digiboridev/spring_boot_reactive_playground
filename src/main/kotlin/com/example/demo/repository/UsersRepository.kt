package com.example.demo.repository

import com.example.demo.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersRepository : MongoRepository<User, String> {
    fun findByEmail(email: String): User?
    fun findByEmailAndPassword(email: String, password: String): User?
    fun searchByEmail(email: String): List<User>
    fun searchByFirstName(firstName: String): List<User>
    fun searchByLastName(lastName: String): List<User>
    fun searchByFirstNameOrLastName(firstName: String, lastName: String): List<User>
    fun searchByFirstNameOrLastNameOrEmail(firstName: String, lastName: String, email: String): List<User>
}


