package com.digiboridev.rxpg.repository

import com.digiboridev.rxpg.model.Product
import com.mongodb.lang.NonNullApi
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository


@Repository
interface ProductsRepository : CoroutineCrudRepository<Product, String> {
    suspend fun findAllBy(firstName: TextCriteria): Flow<Product>
    suspend fun findAllByCategoryIds(categoryId: String): Flow<Product>
    suspend fun findAllByBrandId(brandId: String): Flow<Product>
    suspend fun findByCategoryIdsOrBrandId(categoryId: String?, brandId: String?): Flow<Product>
    suspend fun findByCategoryIdsAndBrandId(categoryId: String?, brandId: String?): Flow<Product>
}


