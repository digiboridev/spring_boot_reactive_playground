package com.example.demo.persons

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.Date


@RestController
@RequestMapping("/api")
class PersonsController(val personsRepository: PersonsRepository) {

    // Validation exception handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): Map<String, Any> {
        val errors = e.bindingResult.fieldErrors.map { it.defaultMessage }
        return mapOf(
            "timestamp" to Date(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "message" to "Invalid request",
            "errors" to errors,
        )
    }

    @GetMapping("/")
    fun hello(): String = "Im alive!"


    @GetMapping("/persons")
    fun getPersons(): List<Person> {
        return listOf(Person("1", "John", 20), Person("2", "Jane", 30))
    }

    data class NewPersonDTO(
        @field:NotNull(message = "Name is null")
        @field:NotBlank(message = "Name is empty")
        val name: String? = null,
        @field:NotNull(message = "Age is null")
        @field:Min(18, message = "Age must be greater than 18")
        @field:Max(value = 150, message = "Age should not be greater than 150")
        val age: Int? = null,
        val address: String? = null
    )

    @PostMapping("/persons")
    fun createPerson(@RequestBody @Valid request: NewPersonDTO): Person {
        val nameFree: Boolean = personsRepository.findByName(request.name!!).isEmpty()
        if (!nameFree) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is already taken")
        }

        val newPerson = Person(name = request.name, age = request.age!!)
        val personEntity = personsRepository.save(newPerson)
        return personEntity
    }
}