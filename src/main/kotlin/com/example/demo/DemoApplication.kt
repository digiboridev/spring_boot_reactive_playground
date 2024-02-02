package com.example.demo

import com.example.demo.core.exceptions.BaseException
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import java.time.Instant


@SpringBootApplication
@EnableMongoAuditing
@EnableMethodSecurity
class DemoApplication

@RestController
@RequestMapping("/api")
class GlobalController {
    @GetMapping("/health-check")
    fun healthCheck(): String = "Im alive!"
}


fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
    println("DemoApplication Started")
}

