package com.example.demo.core

import com.example.demo.service.JWTService
import jakarta.servlet.DispatcherType
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
    private val systemPath = "/api/system/**"
    private val healthCheckPath = "/api/health-check"
    private val eventsPath = "/api/events/**"

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val authFilter = AuthorizationFilter(jwtService, listOf(authPath, usersPath, systemPath, eventsPath))

        http
            .cors { it.disable() }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)
            // forward errors, redirects or async events that blocked by default if denyAll in the end
            .authorizeHttpRequests {
                it.dispatcherTypeMatchers(
                    DispatcherType.ERROR, DispatcherType.ASYNC, DispatcherType.INCLUDE, DispatcherType.FORWARD
                ).permitAll()
            }
            .authorizeHttpRequests { it.requestMatchers(healthCheckPath).permitAll() }
            .authorizeHttpRequests { it.requestMatchers(systemPath).hasRole("ADMIN") }
            .authorizeHttpRequests { it.requestMatchers(authPath).anonymous() }
            .authorizeHttpRequests { it.requestMatchers(usersPath).authenticated() }
            .authorizeHttpRequests { it.requestMatchers(eventsPath).authenticated() }
            // map exceptions properly, by default it always returns 403
            .exceptionHandling {
                it.accessDeniedHandler { _, response, _ -> response.sendError(403) }
                it.authenticationEntryPoint { _, response, _ -> response.sendError(401) }
            }
            // deny all other requests
            .authorizeHttpRequests { it.anyRequest().denyAll() }

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
            println("Token parsing error: $e")
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

