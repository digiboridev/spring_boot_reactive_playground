package com.digiboridev.rxpg.core

import com.digiboridev.rxpg.service.JWTService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    val authManager: ReactiveAuthenticationManager,
    val authConverter: ServerAuthenticationConverter
) {
    private val authPath = "/api/auth/**"
    private val usersPath = "/api/users/**"
    private val systemPath = "/api/system/**"
    private val healthCheckPath = "/api/health-check"
    private val eventsPath = "/api/events/**"

    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        val authenticationWebFilter = AuthenticationWebFilter(authManager)
        authenticationWebFilter.setServerAuthenticationConverter(authConverter)

        return http
            .cors { it.disable() }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .authorizeExchange {
                it.pathMatchers(HttpMethod.GET, healthCheckPath).permitAll()
                it.pathMatchers(systemPath).hasRole("ADMIN")
                it.pathMatchers(usersPath).authenticated()
//                it.pathMatchers(eventsPath).authenticated()
                it.anyExchange().permitAll()
            }
            .build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}


@Component
class AuthManager : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        println("CustomAuthManager: $authentication")
        return Mono.just(authentication)
    }
}


@Component
class AuthConverter(val jwtService: JWTService) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        val request = exchange.request
        val headerAuth = request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.substring(7)
        val cookieAuth = request.cookies.getFirst("Authorization")?.value

        println("headerAuth: $headerAuth")
        println("cookieAuth: $cookieAuth")

        if (headerAuth != null || cookieAuth != null) {
            try {
                val token = headerAuth ?: cookieAuth!!
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


/// Custom authentication object
data class AppAuthentication(
    val id: String,
    val email: String,
    private val authorities: Collection<GrantedAuthority>,
    private var authenticated: Boolean = true
) : Authentication {

    override fun getName() = email
    override fun getAuthorities() = authorities
    override fun getPrincipal() = id
    override fun isAuthenticated() = authenticated
    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }

    override fun getCredentials() = null
    override fun getDetails() = null
}

