package com.example.demo.core

import com.example.demo.service.JWTService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(val jwtService: JWTService) {
    private val authPath = "/api/auth/**"
    private val usersPath = "/api/users/**"

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val authFilter = AuthorizationFilter(jwtService, listOf(authPath,usersPath))

        http
            .cors { it.disable() }
            .csrf { it.disable() }
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)
            // can reach auth path only if not authenticated yet
            .authorizeHttpRequests { it.requestMatchers(authPath).anonymous() }
            // can reach users path if authenticated with any authority
            .authorizeHttpRequests { it.requestMatchers(usersPath).authenticated() }
            // pass all other requests, like error pages, health checks, etc.
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.NEVER) }

        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}

/// Filter for parsing and inject authentication object into context
private class AuthorizationFilter(val jwtService: JWTService, val patterns: List<String>) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Skip if the request shouldn't be filtered
        if (!shouldFilter(request)) return filterChain.doFilter(request, response)

        // Extract token from header or a cookie
        // Usefully for testing with Postman
        val headerAuth = request.getHeader("Authorization")?.substring(7)
        val cookieAuth = request.cookies?.find { it.name == "Authorization" }?.value
        println("headerAuth: $headerAuth")
        println("cookieAuth: $cookieAuth")

        // Try parsing the token and setting authentication to the context
        try {
            val token = headerAuth ?: cookieAuth ?: throw tokenErr

            val claims = jwtService.extractClaims(token)
            println("claims: $claims")

            val id = claims.getOrElse("sub") { throw tokenErr } as String
            val role = claims.getOrElse("role") { throw tokenErr } as String
            val email = claims.getOrElse("email") { throw tokenErr } as String

            val authorities = AuthorityUtils.createAuthorityList("ROLE_$role")
            val authentication = AppAuthentication(id, email, authorities)
            SecurityContextHolder.getContext().authentication = authentication
            println("authentication: $authentication")
        } catch (e: Exception) {
            return response.sendError(401, e.message)
        }

        // Continue with the request
        filterChain.doFilter(request, response)
    }

    // Check if the request should be filtered by patterns list
    private fun shouldFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        val matcher = AntPathMatcher()
        val match = patterns.any { matcher.match(it, path) }
        return match
    }

    private val tokenErr = RuntimeException("invalid token")
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

