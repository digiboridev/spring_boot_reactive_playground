package com.digiboridev.rxpg.data.repository

import com.digiboridev.rxpg.data.dto.BrandData
import com.digiboridev.rxpg.data.model.Brand
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface BrandsRepository : CoroutineCrudRepository<Brand, String>, CustomizedBrandsRepository {
    suspend fun findAllBrandBy(): Flow<Brand>
    suspend fun findAllBrandDataBy(): Flow<BrandData>

    suspend fun findAllBrandBy(text: TextCriteria, limit: Int): Flow<Brand>
    suspend fun findAllBrandDataBy(text: TextCriteria, limit: Int): Flow<BrandData>

    suspend fun findBrandById(id: String): Brand?
    suspend fun findBrandDataById(id: String): BrandData?
}

interface CustomizedBrandsRepository {
    suspend fun vectorTextSearch(embeddings: List<Double>, limit: Int): Flow<BrandData>
}

class CustomizedBrandsRepositoryImpl(private val template: ReactiveMongoTemplate) : CustomizedBrandsRepository {
    override suspend fun vectorTextSearch(embeddings: List<Double>, limit: Int): Flow<BrandData> {
        val json =
            "  { \"\$vectorSearch\": { \"index\": \"vector_index\", \"path\": \"vec\", \"queryVector\": $embeddings, \"numCandidates\": ${limit * 10}, \"limit\": $limit } } "

        val stage = Aggregation.stage(json)
        val aggregation = Aggregation.newAggregation(stage)
        return template.aggregate(aggregation, "brands", BrandData::class.java).asFlow()
    }
}

