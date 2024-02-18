package com.digiboridev.rxpg.controller

import com.digiboridev.rxpg.data.dto.ErrorResponse
import com.digiboridev.rxpg.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Tag(name = "Auth", description = "Auth endpoints")
@RestController()
@RequestMapping("/api/auth")
class AuthController(val authService: AuthService) {

    data class SuccessResponse(
        val token: String
    )

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
                responseCode = "409", description = "Conflict",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/signUp")
    suspend fun signUp(@RequestBody @Valid request: SignUpRequest): ResponseEntity<SuccessResponse> {
        val token = authService.signUp(request.email!!, request.password!!, request.firstName!!, request.lastName!!)

        return ResponseEntity.ok().headers {
            it.set(HttpHeaders.AUTHORIZATION, "Bearer $token")
            it.set(HttpHeaders.SET_COOKIE, "Authorization=Bearer $token; Path=/; HttpOnly; SameSite=Strict; Secure")
        }.body(SuccessResponse(token))
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
    
    @Operation(
        summary = "Sign in",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [Content(
                examples = [ExampleObject(
                    value = "{\n" +
                            "   \"email\":\"dtailor@mail.com\",\n" +
                            "   \"password\":\"123123\"\n" +
                            "}"
                )]
            )]
        ),
        responses = [
            ApiResponse(
                responseCode = "409", description = "Conflict",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/signIn")
    suspend fun signIn(@RequestBody @Valid request: SignInRequest): ResponseEntity<SuccessResponse> {
        val token = authService.signIn(request.email!!, request.password!!)

        return ResponseEntity.ok().headers {
            it.set(HttpHeaders.AUTHORIZATION, "Bearer $token")
            it.set(HttpHeaders.SET_COOKIE, "Authorization=Bearer $token; Path=/; HttpOnly; SameSite=Strict; Secure")
        }.body(SuccessResponse(token))
    }

}