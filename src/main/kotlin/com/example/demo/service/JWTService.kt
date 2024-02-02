package com.example.demo.service


import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey


@Component
class JWTService {
    private val secret = "461379486794f3b5752e1ffcf597a274ac31c3afb00987a1e31b6b78a087f552"

    fun generateToken(claims: Map<String, Any>, subject: String, expires: Instant): String {
        val token = Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(Date())
            .expiration(Date.from(expires))
            .signWith(getSigningKey())
            .compact()
        return token
    }

    fun extractClaims(token: String): Map<String,Any> {
        val data = Jwts.parser()
            .verifyWith(getSigningKey()) // <----
            .build()
            .parseSignedClaims(token);
        return data.payload
    }

    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

}