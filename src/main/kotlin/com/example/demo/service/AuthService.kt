package com.example.demo.service

import com.example.demo.core.exceptions.AuthExceptions
import com.example.demo.model.User
import com.example.demo.repository.UsersRepository
import jakarta.annotation.PostConstruct
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
    private fun fillData() {
        println("Filling data")
        usersRepository.saveAll(mockedUsers())
    }


    private fun mockedUsers() = listOf(
        // Drone Tailor
        User(email = "dtailor@mail.com",password = passwordEncoder.encode("123123"),firstName = "Drone",lastName = "Tailor"),
        // John Doe
        User(email = "jdoe@mail.com",password = passwordEncoder.encode("123123"),firstName = "John",lastName = "Doe"),
        // Antony Swan
        User(email = "aswan@mail.com",password = passwordEncoder.encode("123123"),firstName = "Antony",lastName = "Swan"),
        // Jane Doe
        User(email = "janedoe@mail.com",password = passwordEncoder.encode("123123"),firstName = "Jane",lastName = "Doe"),
        // Mary Johnson
        User(email = "marryjohnson@mail.com",password = passwordEncoder.encode("123123"),firstName = "Mary",lastName = "Johnson"),
        // John Smith
        User(email = "jsmith@mail.com",password = passwordEncoder.encode("123123"),firstName = "John",lastName = "Smith"),
        // Mary Smith
        User(email = "masmith@mail.com",password = passwordEncoder.encode("123123"),firstName = "Mary",lastName = "Smith"),
        // Antony Johnson
        User(email = "anjohnson@mail.com",password = passwordEncoder.encode("123123"),firstName = "Antony",lastName = "Johnson"),
        // Jane Smith
        User(email = "janesmith@mail.com",password = passwordEncoder.encode("123123"),firstName = "Jane",lastName = "Smith"),
        // Antony Doe
        User(email = "andoe@mail.com",password = passwordEncoder.encode("123123"),firstName = "Antony",lastName = "Doe"),
        // Mary Doe
        User(email = "mardoe@mail.com",password = passwordEncoder.encode("123123"),firstName = "Mary",lastName = "Doe"),
        // Jane Johnson
        User(email = "janejonson@mail.com",password = passwordEncoder.encode("123123"),firstName = "Jane",lastName = "Johnson"),
        // John Johnson
        User(email = "jonjonson@mail.com",password = passwordEncoder.encode("123123"),firstName = "John",lastName = "Johnson"),
        // Garry Brown
        User(email = "garrybrown@mail.com",password = passwordEncoder.encode("123123"),firstName = "Garry",lastName = "Brown"),
        // Tony Miller
        User(email = "tonymiller@mail.com",password = passwordEncoder.encode("123123"),firstName = "Tony",lastName = "Miller"),
        // William Sanders
        User(email = "willsander@mail.com",password = passwordEncoder.encode("123123"),firstName = "William",lastName = "Sanders"),
        // Sam Robinson
        User(email = "samrobinson@mail.com",password = passwordEncoder.encode("123123"),firstName = "Sam",lastName = "Robinson"),
        // Amy White
        User(email = "amywhite@mail.com",password = passwordEncoder.encode("123123"),firstName = "Amy",lastName = "White"),
        // Mary Black
        User(email = "maryblack@mail.com",password = passwordEncoder.encode("123123"),firstName = "Mary",lastName = "Black"),
        // Jane Green
        User(email = "janegreen@mail.com",password = passwordEncoder.encode("123123"),firstName = "Jane",lastName = "Green"),
        // Adam Nelson
        User(email = "adamnilson@mail.com",password = passwordEncoder.encode("123123"),firstName = "Adam",lastName = "Nelson"),
    )
}


