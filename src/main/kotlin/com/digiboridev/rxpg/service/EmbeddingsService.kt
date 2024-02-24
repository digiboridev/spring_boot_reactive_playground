package com.digiboridev.rxpg.service

import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.stereotype.Service

@Service
class EmbeddingsService(
    val embeddingClient: EmbeddingClient
) {
    fun getEmbeddings(text: String): List<Double> {
        return embeddingClient.embed(text)
    }
}