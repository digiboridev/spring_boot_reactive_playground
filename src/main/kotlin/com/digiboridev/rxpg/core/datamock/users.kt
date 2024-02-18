package com.digiboridev.rxpg.core.datamock

import com.digiboridev.rxpg.data.model.User
import com.digiboridev.rxpg.data.repository.UsersRepository
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component


@Component
class UsersDataMock(
    val usersRepository: UsersRepository,
    val passwordEncoder: PasswordEncoder,
) {

//    @OptIn(DelicateCoroutinesApi::class)
//    @PostConstruct
//    fun postConstruct() {
//        println("ProductsDataMock postConstruct...")
//        GlobalScope.launch {
//            println("ProductsDataMock initializing...")
//            fillData(usersRepository, passwordEncoder)
//            println("ProductsDataMock initialized")
//        }
//    }
}

suspend fun fillData(
    usersRepository: UsersRepository,
    passwordEncoder: PasswordEncoder,
) {
    usersRepository.deleteAll()

    val users = listOf(
        // Drone Tailor
        User(
            email = "dtailor@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Drone",
            lastName = "Tailor"
        ),
        // John Doe
        User(
            email = "jdoe@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "John",
            lastName = "Doe"
        ),
        // Antony Swan
        User(
            email = "aswan@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Antony",
            lastName = "Swan"
        ),
        // Jane Doe
        User(
            email = "janedoe@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Jane",
            lastName = "Doe"
        ),
        // Mary Johnson
        User(
            email = "marryjohnson@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Mary",
            lastName = "Johnson"
        ),
        // John Smith
        User(
            email = "jsmith@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "John",
            lastName = "Smith"
        ),
        // Mary Smith
        User(
            email = "masmith@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Mary",
            lastName = "Smith"
        ),
        // Antony Johnson
        User(
            email = "anjohnson@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Antony",
            lastName = "Johnson"
        ),
        // Jane Smith
        User(
            email = "janesmith@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Jane",
            lastName = "Smith"
        ),
        // Antony Doe
        User(
            email = "andoe@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Antony",
            lastName = "Doe"
        ),
        // Mary Doe
        User(
            email = "mardoe@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Mary",
            lastName = "Doe"
        ),
        // Jane Johnson
        User(
            email = "janejonson@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Jane",
            lastName = "Johnson"
        ),
        // John Johnson
        User(
            email = "jonjonson@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "John",
            lastName = "Johnson"
        ),
        // Garry Brown
        User(
            email = "garrybrown@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Garry",
            lastName = "Brown"
        ),
        // Tony Miller
        User(
            email = "tonymiller@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Tony",
            lastName = "Miller"
        ),
        // William Sanders
        User(
            email = "willsander@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "William",
            lastName = "Sanders"
        ),
        // Sam Robinson
        User(
            email = "samrobinson@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Sam",
            lastName = "Robinson"
        ),
        // Amy White
        User(
            email = "amywhite@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Amy",
            lastName = "White"
        ),
        // Mary Black
        User(
            email = "maryblack@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Mary",
            lastName = "Black"
        ),
        // Jane Green
        User(
            email = "janegreen@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Jane",
            lastName = "Green"
        ),
        // Adam Nelson
        User(
            email = "adamnilson@mail.com",
            password = passwordEncoder.encode("123123"),
            firstName = "Adam",
            lastName = "Nelson"
        ),
    )

}