package com.digiboridev.rxpg

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.mono
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.core.context.ReactiveSecurityContextHolder
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
    private val sharedFlow = MutableSharedFlow<String>()

    override fun handle(session: WebSocketSession): Mono<Void> {
        println("Inbound session: ${session.id}")

        return ReactiveSecurityContextHolder.getContext().flatMap {
            mono {
                println("Prepared session: ${session.id}")
                it.authentication
            }
        }.flatMap { auth ->
            println("Auth: $auth")

            val input = mono { handleWithCoroutines(session.receive().map { it.payloadAsText }.asFlow(), session.id) }
            val output = session.send(sharedFlow.asFlux().map { session.textMessage(it) })

            Mono.zip(output, input)
        }.then()
    }

    private suspend fun handleWithCoroutines(inboundFlow: Flow<String>, sessionId: String) {
        println("input flow begin: $sessionId")
        inboundFlow.collect { msg ->
            println("Message received: $msg")
            sharedFlow.emit("Hello, $sessionId that sends: $msg")
        }
        println("input flow done: $sessionId")
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


