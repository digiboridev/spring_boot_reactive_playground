package com.digiboridev.rxpg.controller

import com.digiboridev.rxpg.model.Category
import com.digiboridev.rxpg.repository.CategoriesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

// TODO exceptions


@Tag(name = "Categories", description = "Categories endpoints")
@RestController
@RequestMapping("/api/categories")
class CategoriesController(val repository: CategoriesRepository) {

    @GetMapping("/")
    suspend fun getTopLevel(): Flow<Category> {
        return repository.findAllByParentIdIsNull()
    }

    @GetMapping("/{id}")
    suspend fun geById(@PathVariable id: String): Category {
        return repository.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
    }

    @GetMapping("/{parentId}/")
    suspend fun getByParentId(@PathVariable parentId: String): Flow<Category> {
        return repository.findAllByParentId(parentId)
    }

}

