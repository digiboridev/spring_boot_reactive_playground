package com.digiboridev.rxpg.core.security

import com.digiboridev.rxpg.service.JWTService
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthConverter(val jwtService: JWTService) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication?> {

        val request = exchange.request
        val headerAuth = request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.substring(7)
        val cookieAuth = request.cookies.getFirst("Authorization")?.value?.substring(7)
        val queryAuth = request.queryParams.getFirst("token")?.substring(7)

        println("headerAuth: $headerAuth")
        println("cookieAuth: $cookieAuth")
        println("queryAuth: $queryAuth")

        if (headerAuth != null || cookieAuth != null || queryAuth != null) {
            try {
                val token = headerAuth ?: cookieAuth ?: queryAuth!!
                val claims = jwtService.extractClaims(token)
                println("claims: $claims")

                val id = claims["sub"] as String
                val role = claims["role"] as String
                val email = claims["email"] as String

                val authorities = listOf(GrantedAuthority { "ROLE_$role" })
                val auth = AppAuthentication(id, email, authorities)
                return Mono.just(auth)
            } catch (e: Exception) {
                println("Token parsing error: $e")
            }
        }

        return Mono.empty()
    }
}