package com.digiboridev.rxpg

import com.digiboridev.rxpg.core.WSHandler
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping


@Tag(name = "Global", description = "Global endpoints")
@RestController
@RequestMapping("/api")
private class GlobalController {
    @GetMapping("/health-check")
    fun healthCheck(): String = "Im alive!"

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/system/admin-check")
    fun adminCheck(): String = "Hello sir!"
}

@SpringBootApplication
@EnableReactiveMongoAuditing
@EnableReactiveMethodSecurity
class RxpgApplication {
    @Bean
    fun wsMapping(): HandlerMapping {
        val map = mapOf("/testWS" to WSHandler())
        val order = -1 // before annotated controllers
        return SimpleUrlHandlerMapping(map, order)
    }
}

fun main(args: Array<String>) {
    runApplication<RxpgApplication>(*args)
}



