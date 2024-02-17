package com.digiboridev.rxpg.controller

import com.digiboridev.rxpg.core.BaseExceptionResponse
import com.digiboridev.rxpg.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@Tag(name = "Auth", description = "Auth endpoints")
@RestController()
@RequestMapping("/api/auth")
class AuthController(
    val authService: AuthService
) {
    data class SignUpRequest(
        @field:NotNull(message = "Must include email")
        @field:NotBlank(message = "Name is empty")
        @field:Email(message = "Email is invalid")
        val email: String?,

        @field:NotNull(message = "Must include password")
        @field:NotBlank(message = "Password is empty")
        @field:Min(value = 8, message = "Password must be at least 8 characters long")
        val password: String?,

        @field:NotNull(message = "Must include first name")
        @field:NotBlank(message = "First name is empty")
        val firstName: String?,

        @field:NotNull(message = "Must include last name")
        @field:NotBlank(message = "Last name is empty")
        val lastName: String?,
    )

    @Operation(
        summary = "Sign up",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Sign up successful",
                content = [Content(schema = Schema(implementation = String::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [Content(schema = Schema(implementation = BaseExceptionResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Email already taken",
                content = [Content(schema = Schema(implementation = BaseExceptionResponse::class))]
            )
        ]
    )
    @PostMapping("/signUp")
    suspend fun signUp(@RequestBody @Valid request: SignUpRequest): ResponseEntity<Any> {
        val token = authService.signUp(request.email!!, request.password!!, request.firstName!!, request.lastName!!)

        return ResponseEntity.ok().headers {
            it.set(HttpHeaders.AUTHORIZATION, "Bearer $token")
            it.set(HttpHeaders.SET_COOKIE, "Authorization=Bearer $token; Path=/; HttpOnly; SameSite=Strict; Secure")
        }.body(mapOf("token" to token))
    }

    data class SignInRequest(
        @field:NotNull(message = "Name is null")
        @field:NotBlank(message = "Name is empty")
        @field:Email(message = "Email is invalid")
        val email: String?,
        @field:NotNull(message = "Password is null")
        @field:NotBlank(message = "Password is empty")
        @field:Min(value = 8, message = "Password must be at least 8 characters long")
        val password: String?,
    )

    @PostMapping("/signIn")
    suspend fun signIn(@RequestBody @Valid request: SignInRequest): ResponseEntity<Any> {
        val token = authService.signIn(request.email!!, request.password!!)

        return ResponseEntity.ok().headers {
            it.set(HttpHeaders.AUTHORIZATION, "Bearer $token")
            it.set(HttpHeaders.SET_COOKIE, "Authorization=Bearer $token; Path=/; HttpOnly; SameSite=Strict; Secure")
        }.body(mapOf("token" to token))
    }

}