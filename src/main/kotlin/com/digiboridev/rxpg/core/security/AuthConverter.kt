package com.digiboridev.rxpg.core.security

import com.digiboridev.rxpg.data.valueObject.Role
import com.digiboridev.rxpg.service.JWTService
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthConverter(val jwtService: JWTService) : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange): Mono<Authentication?> {

       return mono {
            val request = exchange.request
            try {
                val token = headerAuth(request) ?: cookieAuth(request) ?: queryAuth(request) ?: return@mono null
                val claims = jwtService.extractClaims(token)
                println("claims: $claims")

                val id = claims["sub"] as String
                val role = claims["role"] as String
                val email = claims["email"] as String

                val authorities = listOf(GrantedAuthority { "ROLE_$role" })

                val auth = AppAuthentication(id, email, Role.valueOf(role),authorities)
                auth
            } catch (e: Exception) {
                println("Token parsing error: $e")
                null
            }
        }

    }


    private fun headerAuth (request: ServerHttpRequest): String? {
        return request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.substring(7)
    }

    private fun cookieAuth (request: ServerHttpRequest): String? {
        return request.cookies.getFirst("Authorization")?.value?.substring(7)
    }

    private fun queryAuth (request: ServerHttpRequest): String? {
        return request.queryParams.getFirst("token")?.substring(7)
    }
}