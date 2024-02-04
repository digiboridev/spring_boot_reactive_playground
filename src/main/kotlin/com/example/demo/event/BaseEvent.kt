package com.example.demo.event

import org.springframework.context.ApplicationEvent

class BaseEvent(
     private val source: Any,
     val payload: Any
) : ApplicationEvent (source)