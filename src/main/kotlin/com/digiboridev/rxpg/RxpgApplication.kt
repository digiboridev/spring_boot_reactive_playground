package com.digiboridev.rxpg

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
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/api")
private class GlobalController {
    @GetMapping("/health-check")
    fun healthCheck(): String = "Im alive!"

    @GetMapping("/system/admin-check")
    fun adminCheck(): String = "Hello sir!"

}

class WSHandler : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> {
        println("Inbound session: ${session.id}")
        return session.receive().flatMap {
            println(it.payloadAsText)
            session.send(Mono.just(session.textMessage("Hello, " + it.payloadAsText)))
        }.then()
    }
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


