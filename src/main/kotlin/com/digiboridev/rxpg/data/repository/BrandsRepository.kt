package com.digiboridev.rxpg.data.repository

import com.digiboridev.rxpg.data.model.Brand
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface BrandsRepository : CoroutineCrudRepository<Brand, String> {
    suspend fun findAllBy(firstName: TextCriteria): Flow<Brand>
}