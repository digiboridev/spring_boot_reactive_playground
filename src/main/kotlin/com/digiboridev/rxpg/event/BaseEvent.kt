package com.digiboridev.rxpg.event


data class BaseEvent(
     private val source: Any,
     val payload: Any
)