package com.digiboridev.rxpg.data.event


data class BaseEvent(
     private val source: Any,
     val payload: Any
)