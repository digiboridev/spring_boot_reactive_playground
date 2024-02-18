package com.digiboridev.rxpg.service

import com.digiboridev.rxpg.core.exceptions.AuthException
import com.digiboridev.rxpg.core.exceptions.ResourceException
import com.digiboridev.rxpg.data.dto.AuthResponse
import com.digiboridev.rxpg.data.model.User
import com.digiboridev.rxpg.data.model.UserSession
import com.digiboridev.rxpg.data.repository.UserSessionRepository
import com.digiboridev.rxpg.data.repository.UsersRepository
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AuthService(
    val jwtService: JWTService,
    val usersRepository: UsersRepository,
    val passwordEncoder: PasswordEncoder,
    val userSessionRepository: UserSessionRepository
) {

    suspend fun signUp(email: String, password: String, firstName: String, lastName: String): AuthResponse {
        usersRepository.findByEmail(email) ?: throw AuthException.emailAlreadyTaken()

        val encodedPassword = passwordEncoder.encode(password)
        val userEntity = usersRepository.save(
            User(
                email = email,
                password = encodedPassword,
                firstName = firstName,
                lastName = lastName
            )
        )

        return createSessionTokens(userEntity)
    }

    suspend fun signIn(email: String, password: String): AuthResponse {
        val userEntity = usersRepository.findByEmail(email) ?: throw AuthException.invalidCredentials("email")

        if (!passwordEncoder.matches(password, userEntity.password)) {
            throw AuthException.invalidCredentials("password")
        }

        return createSessionTokens(userEntity)
    }

    suspend fun refreshToken(refreshToken: String): AuthResponse {
        val claims: Map<String, Any>

        try {
            claims = jwtService.extractClaims(refreshToken)
        } catch (e: Exception) {
            if (e is ExpiredJwtException) throw AuthException.expiredToken()
            throw AuthException.invalidToken()
        }

        val sessionId = claims["sessionId"] as String
        val userSession = userSessionRepository.findById(sessionId) ?: throw AuthException.expiredSession()
        val userEntity = usersRepository.findById(userSession.userId) ?: throw ResourceException.notFound("User")

        userSessionRepository.delete(userSession)

        return createSessionTokens(userEntity)
    }


    // Creates a new session and a new pair of session tokens
    private suspend fun createSessionTokens(userEntity: User): AuthResponse {
        val userSession = UserSession(userId = userEntity.id, expiresAt = refreshExp())
        userSessionRepository.save(userSession)

        val refreshToken = jwtService.generateToken(
            mapOf("sessionId" to userSession.id),
            userEntity.id,
            refreshExp()
        )

        val accessToken = jwtService.generateToken(
            mapOf("role" to userEntity.role, "email" to userEntity.email),
            userEntity.id,
            accessExp()
        )

        return AuthResponse(accessToken, refreshToken)
    }


    // Returns the expiration time of the tokens
    private fun accessExp() = Instant.now().plusSeconds(60 * 60)
    private fun refreshExp() = Instant.now().plusSeconds(60 * 60 * 24 * 7)

}


