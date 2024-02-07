package com.digiboridev.rxpg.repository

import com.digiboridev.rxpg.dto.PersonalUserInfo
import com.digiboridev.rxpg.dto.PublicUserInfo
import com.digiboridev.rxpg.model.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersRepository : CoroutineCrudRepository<User, String> {
    suspend fun findByEmail(email: String): User?
    suspend fun findAllBy(firstName: TextCriteria): Flow<PublicUserInfo>
    suspend fun findPersonalUserInfoById(id: String): PersonalUserInfo?
    suspend fun findPublicUserById(id: String): PublicUserInfo?
    suspend fun findAllPublicUsersBy(pageable: Pageable): Flow<PublicUserInfo>
}


