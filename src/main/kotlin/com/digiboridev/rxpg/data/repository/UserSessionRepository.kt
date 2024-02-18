package com.digiboridev.rxpg.data.repository

import com.digiboridev.rxpg.data.model.UserSession
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserSessionRepository : CoroutineCrudRepository<UserSession, String> {
    suspend fun findByUserId(userId: String): UserSession?
    suspend fun deleteByUserId(userId: String)
    suspend fun deleteByExpiresAtBefore(expiresAt: Long)
}