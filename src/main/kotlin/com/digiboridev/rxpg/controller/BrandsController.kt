package com.digiboridev.rxpg.controller

import com.digiboridev.rxpg.core.exceptions.ResourceException
import com.digiboridev.rxpg.model.Brand
import com.digiboridev.rxpg.repository.BrandsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Tag(name = "Brands", description = "Brands endpoints")
@RestController
@RequestMapping("/api/brands")
class BrandsController(val repository: BrandsRepository) {

    @GetMapping("/")
    suspend fun getAll(): Flow<Brand> {
        return repository.findAll()
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: String): Brand {
        return repository.findById(id) ?: throw ResourceException.notFound("Brand")
    }

    @GetMapping("/search/{query}")
    suspend fun search(@PathVariable query: String): Flow<Brand> {
        val criteria = TextCriteria.forDefaultLanguage().matching(query)
        return repository.findAllBy(criteria)
    }

}

