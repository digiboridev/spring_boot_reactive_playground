package com.digiboridev.rxpg.repository

import com.digiboridev.rxpg.model.Category
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface CategoriesRepository : CoroutineCrudRepository<Category, String> {
    suspend fun findAllByParentId(parentId: String): Flow<Category>
    suspend fun findAllByParentIdIsNull(): Flow<Category>
}