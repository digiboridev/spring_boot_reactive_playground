package com.digiboridev.rxpg.repository

import com.digiboridev.rxpg.model.Brand
import com.digiboridev.rxpg.model.Product
import com.digiboridev.rxpg.model.User
import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface ProductsRepository : CoroutineCrudRepository<Product, String> {
    suspend fun findAllBy(firstName: TextCriteria): Flow<Brand>
    suspend fun findAllByCategoryIds(categoryId: String): Flow<Product>
    suspend fun findAllByBrandId(brandId: String): Flow<Product>
    abstract fun findAllByCategoryIdsOrBrandId(categoryId: String?, brandId: String?): Flow<Product>
}