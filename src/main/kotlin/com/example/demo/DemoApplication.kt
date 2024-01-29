package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
@EnableMongoAuditing
class DemoApplication


@RestController
@RequestMapping("/api")
class GlobalController {
    @GetMapping("/")
    fun healthCheck(): String = "Im alive!"
}


fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
    println("DemoApplication Started")
}

