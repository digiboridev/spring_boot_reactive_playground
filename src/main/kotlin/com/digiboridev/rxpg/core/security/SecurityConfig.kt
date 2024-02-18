package com.digiboridev.rxpg.core.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter


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
//            .oauth2Login {
//                it.authenticationSuccessHandler { webFilterExchange, authentication ->
//                    mono {
//                        if (authentication is OAuth2AuthenticationToken) {
//                            val registrationId = authentication.authorizedClientRegistrationId
//                            val email = authentication.principal.attributes["email"] as String
//                            val emailVerified = authentication.principal.attributes["email_verified"] as Boolean
//                            val subjectId = authentication.principal.attributes["sub"] as String
//                            val name = authentication.principal.attributes["name"] as String
//                            val picture = authentication.principal.attributes["picture"] as String
//                            print("OAuth2Login: $registrationId, $email, $emailVerified, $subjectId, $name, $picture")
//                        }
//                        // TODO upsert user
//                        webFilterExchange.exchange.response.headers.location = URI("/api/health-check")
//                        webFilterExchange.exchange.response.statusCode = org.springframework.http.HttpStatus.FOUND
//                        webFilterExchange.exchange.response.headers.set(HttpHeaders.AUTHORIZATION, "Bearer as")
//                    }.then()
//                }
//            }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .authorizeExchange {
                it.pathMatchers("/login").permitAll()
                it.pathMatchers(HttpMethod.GET, healthCheckPath).permitAll()
                it.pathMatchers(systemPath).hasRole("ADMIN")
                it.pathMatchers(usersPath).authenticated()
                it.pathMatchers(eventsPath).authenticated()
                it.anyExchange().permitAll()
            }
            .build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}


