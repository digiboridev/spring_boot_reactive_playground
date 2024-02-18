package com.digiboridev.rxpg.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

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