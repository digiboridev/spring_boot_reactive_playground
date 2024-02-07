package com.digiboridev.rxpg.service


import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey


@Component
class JWTService{
    @Value("\${jwt.secret}")
    private lateinit var secret: String

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