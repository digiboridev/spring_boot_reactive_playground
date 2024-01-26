package com.example.demo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing
import java.util.*

@SpringBootApplication
@EnableMongoAuditing
class DemoApplication

fun main(args: Array<String>) {
//	runApplication<DemoApplication>(*args)

	val context = runApplication<DemoApplication>(*args)
	val repository = context.getBean(PersonRepository::class.java)

//	repository.save(Person(name = "John_auto1", age = 42))



//	val person2 = repository.findByName("John")
//	println("person2: $person2")
//
//	val persons3 = repository.findByAge(36)
//	println("person3: $persons3")

	var persons  = repository.findAll()
	persons.forEach {
		println("person3: ${it.id}")

		val upd = it.copy(address = "adr2")
		repository.save(upd)

	}

	persons  = repository.findAll()
	persons.forEach {
		println("$it")
	}

	// This is a comment
	println("Hello, world!")
}
