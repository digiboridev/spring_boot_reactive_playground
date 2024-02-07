package com.digiboridev.rxpg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
private class GlobalController {
	@GetMapping("/health-check")
	fun healthCheck(): String = "Im alive!"

	@GetMapping("/system/admin-check")
	fun adminCheck(): String = "Hello sir!"
}

@SpringBootApplication
@EnableReactiveMongoAuditing
@EnableReactiveMethodSecurity
class RxpgApplication

fun main(args: Array<String>) {
	runApplication<RxpgApplication>(*args)
}
