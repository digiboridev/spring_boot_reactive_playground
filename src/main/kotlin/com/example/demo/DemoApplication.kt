package com.example.demo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.stereotype.Component
import java.util.*


interface PolyBean {
    fun foo()
}

class PolyBeanImpl1 : PolyBean {
    override fun foo() {
        println("PolyBeanImpl1")
    }
}

class PolyBeanImpl2 : PolyBean {
    override fun foo() {
        println("PolyBeanImpl2")
    }
}

@Component
class SomeComponent {
	fun doSomething() {
		println("doSomething")
	}
}


@SpringBootApplication
@EnableMongoAuditing
class DemoApplication {
    @Bean("testBean")
    fun testBean(): String {
        return "123"
    }

    @Bean
    fun polyBean(): PolyBean {
        return PolyBeanImpl1()
    }

}

fun main(args: Array<String>) {
//	runApplication<DemoApplication>(*args)

    val context = runApplication<DemoApplication>(*args)
    val repository = context.getBean(PersonRepository::class.java)

    val testBean = context.getBean("testBean", String::class.java)
    println("testBean: $testBean")

    val polyBean = context.getBean(PolyBean::class.java)
    polyBean.foo()

	val someComponent = context.getBean(SomeComponent::class.java)
	someComponent.doSomething()

//	repository.save(Person(name = "John_auto1", age = 42))


//	val person2 = repository.findByName("John")
//	println("person2: $person2")
//
//	val persons3 = repository.findByAge(36)
//	println("person3: $persons3")

    var persons = repository.findAll()
    persons.forEach {
        println("person3: ${it.id}")

        val upd = it.copy(address = "adr2")
        repository.save(upd)

    }

    persons = repository.findAll()
    persons.forEach {
        println("$it")
    }

    // This is a comment
    println("Hello, world!")
}
