package com.digiboridev.rxpg.controller

import com.digiboridev.rxpg.event.BaseEvent
import kotlinx.coroutines.flow.*
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/events")
class EventsController {
    val eventFlow = MutableSharedFlow<BaseEvent>()

    @PostMapping("/publish")
    suspend fun publishEvent(@RequestBody event: Any): ResponseEntity<String> {
        println("Incoming event: $event")
        eventFlow.emit(BaseEvent(this, event))
        return ResponseEntity.ok().body("Event published")
    }

    @GetMapping("/stream-sse", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun streamSSEEvents(): Flow<Any> {
        return flow {
            emit("connected")
            emitAll(eventFlow)
        }
    }

    @GetMapping("/nextEvent")
    suspend fun nextEvent(): BaseEvent {
        return eventFlow.first()
    }
}

