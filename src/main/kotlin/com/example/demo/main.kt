package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*


@SpringBootApplication
@EnableMongoAuditing
@EnableMethodSecurity
private class DemoApplication

@RestController
@RequestMapping("/api")
private class GlobalController {
    @GetMapping("/health-check")
    fun healthCheck(): String = "Im alive!"

    @GetMapping("/system/admin-check")
    fun adminCheck(): String = "Hello sir!"
}


fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
    println("DemoApplication Started")
}

