package com.digiboridev.rxpg.service

import com.digiboridev.rxpg.data.model.Brand
import com.digiboridev.rxpg.data.dto.BrandData
import com.digiboridev.rxpg.data.repository.BrandsRepository
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.stereotype.Service


@Service
class BrandsService(
    val repository: BrandsRepository,
    val embeddingsService: EmbeddingsService
) {

    suspend fun fullTextSearch(text: String): Flow<BrandData> {
        val criteria = TextCriteria.forDefaultLanguage().matching(text)
        return repository.findAllBrandDataBy(criteria,10)
    }

    suspend fun vectorTextSearch(text: String): Flow<BrandData> {
        val embeddings = embeddingsService.getEmbeddings(text)
        return repository.vectorTextSearch(embeddings,10)
    }
}

