package com.digiboridev.rxpg.data.repository

import com.digiboridev.rxpg.data.dto.PersonalUserInfo
import com.digiboridev.rxpg.data.dto.PublicUserInfo
import com.digiboridev.rxpg.data.model.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.query.TextCriteria
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


