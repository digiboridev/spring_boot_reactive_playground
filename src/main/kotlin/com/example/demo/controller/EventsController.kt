package com.example.demo.controller

import com.example.demo.event.BaseEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter


@RestController
@RequestMapping("/api/events")
class EventsController(
    val publisher: ApplicationEventPublisher
) {
    private val emitters = mutableListOf<SseEmitter>()
    private val deferredResults = mutableListOf<DeferredResult<Any>>()

    @EventListener(BaseEvent::class)
    private fun handleEvent(event: BaseEvent) {
        println("Received event: ${event.payload}")
        emitters.forEach { it.send(event.payload) }
        deferredResults.forEach { it.setResult(event.payload) }
    }

    @PostMapping("/publish")
    fun publishEvent(@RequestBody event: Any): ResponseEntity<Any> {
        publisher.publishEvent(BaseEvent(this, event))
        return ResponseEntity.ok().body("Event published")
    }

    @GetMapping("/stream-sse", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamSSEEvents(): SseEmitter {
        val emitter = SseEmitter()
        emitters.add(emitter)
        emitter.send("Connected")
        emitter.onCompletion { emitters.remove(emitter)}
        emitter.onTimeout {emitter.complete()}
        emitter.onError { emitter.complete()}
        return emitter
    }

    @GetMapping("/nextEvent")
    fun nextEvent(): DeferredResult<Any> {
        val deferredResult = DeferredResult<Any>(5000)
        deferredResults.add(deferredResult)
        deferredResult.onCompletion { deferredResults.remove(deferredResult) }
        deferredResult.onTimeout { deferredResult.setErrorResult("Timeout") }
        deferredResult.onError { deferredResult.setErrorResult("Error") }
        return deferredResult
    }
}