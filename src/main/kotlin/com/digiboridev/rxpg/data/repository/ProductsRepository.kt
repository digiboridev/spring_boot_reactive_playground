package com.digiboridev.rxpg.data.repository

import com.digiboridev.rxpg.data.model.Product
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.flow
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface ProductsRepository : CoroutineCrudRepository<Product, String>, CustomizedProductsRepository {
    suspend fun findAllBy(text: TextCriteria): Flow<Product>
    suspend fun findAllByCategoryIds(categoryId: String): Flow<Product>
    suspend fun findAllByBrandId(brandId: String): Flow<Product>
}

interface CustomizedProductsRepository {
    suspend fun filterIfPresent(categoryId: String?, brandId: String?): Flow<Product>
}

class CustomizedProductsRepositoryImpl(private val template: ReactiveMongoTemplate) : CustomizedProductsRepository {
    override suspend fun filterIfPresent(categoryId: String?, brandId: String?): Flow<Product> {
        var query = Query()

        if (categoryId != null) query = query.addCriteria(Criteria.where("categoryIds").`in`(categoryId))
        if (brandId != null) query = query.addCriteria(Criteria.where("brandId").`is`(brandId))

        return template.query(Product::class.java).matching(query).flow()
    }
}

