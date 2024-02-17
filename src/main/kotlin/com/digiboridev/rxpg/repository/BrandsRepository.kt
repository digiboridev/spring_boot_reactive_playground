package com.digiboridev.rxpg.repository

import com.digiboridev.rxpg.dto.PublicUserInfo
import com.digiboridev.rxpg.model.Brand
import com.digiboridev.rxpg.model.Product
import com.digiboridev.rxpg.model.User
import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface BrandsRepository : CoroutineCrudRepository<Brand, String> {
    suspend fun findAllBy(firstName: TextCriteria): Flow<Brand>
}