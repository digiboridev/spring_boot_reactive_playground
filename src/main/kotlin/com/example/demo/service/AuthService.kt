package com.example.demo.service

import com.example.demo.core.exceptions.AuthExceptions
import com.example.demo.model.User
import com.example.demo.repository.UsersRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant


@Service
class AuthService(
    val jwtService: JWTService,
    val usersRepository: UsersRepository,
    val passwordEncoder: PasswordEncoder
) {

    fun signUp(email: String, password: String, firstName: String, lastName: String): String {
        usersRepository.findByEmail(email) ?: throw AuthExceptions.EmailAlreadyTaken()

        val encodedPassword = passwordEncoder.encode(password)
        val userEntity = usersRepository.save(
            User(
                email = email,
                password = encodedPassword,
                firstName = firstName,
                lastName = lastName
            )
        )

        return jwtService.generateToken(
            mapOf("role" to userEntity.role, "email" to userEntity.email),
            userEntity.id,
            tExp()
        )
    }

    fun signIn(email: String, password: String): String {
        val userEntity = usersRepository.findByEmail(email) ?: throw AuthExceptions.InvalidCredentials("email")

        if (!passwordEncoder.matches(password, userEntity.password)) {
            throw AuthExceptions.InvalidCredentials("password")
        }

        return jwtService.generateToken(
            mapOf("role" to userEntity.role, "email" to userEntity.email),
            userEntity.id,
            tExp()
        )
    }


    // Returns the base time of expiration of the token
    private fun tExp(): Instant {
        return Instant.now().plusSeconds(60 * 60)
    }


//    @PostConstruct
//    private fun fillData() {
//        val users = mutableListOf<User>()
//
//        // Antony Dick
//        users.add(
//            User(
//                email = "adick@mail.com",
//                password = passwordEncoder.encode("123123"),
//                firstName = "Antony",
//                lastName = "Dick"
//            )
//        )
//
//        // John Doe
//        users.add(
//            User(
//                email = "jdoe@mail.com",
//                password = passwordEncoder.encode("123123"),
//                firstName = "John",
//                lastName = "Doe"
//            )
//        )
//
//        // Drone Tailor
//        users.add(
//            User(
//                email = "dtailor@mail.com",
//                password = passwordEncoder.encode("123123"),
//                firstName = "Drone",
//                lastName = "Tailor"
//            )
//        )
//
//
//        usersRepository.saveAll(users)
//
//    }
}