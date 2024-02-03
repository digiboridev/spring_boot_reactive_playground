package com.example.demo.repository

import com.example.demo.dto.PersonalUserInfo
import com.example.demo.dto.PublicUserInfo
import com.example.demo.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersRepository : MongoRepository<User, String> {
    fun findByEmail(email: String): User?

    fun findAllBy(firstName: TextCriteria): List<PublicUserInfo>

    fun findPersonalUserInfoById(id: String): PersonalUserInfo?
    fun findPublicUserById(id: String): PublicUserInfo?
    fun findAllPublicUsersBy(pageable: Pageable): Page<PublicUserInfo>
}


