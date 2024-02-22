package com.digiboridev.rxpg.core.security

import com.digiboridev.rxpg.data.valueObject.Role
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

/// Custom authentication object
data class AppAuthentication(
    val id: String,
    val email: String,
    val role : Role,
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