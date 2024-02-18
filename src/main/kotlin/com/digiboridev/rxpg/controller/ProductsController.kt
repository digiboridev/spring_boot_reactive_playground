package com.digiboridev.rxpg.controller

import com.digiboridev.rxpg.core.exceptions.ResourceException
import com.digiboridev.rxpg.data.model.Product
import com.digiboridev.rxpg.data.repository.ProductsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.web.bind.annotation.*


@Tag(name = "Products", description = "Products endpoints")
@RestController
@RequestMapping("/api/products")
class ProductsController(val repository: ProductsRepository) {

    @GetMapping("/")
    suspend fun getAll(): Flow<Product> {
        return repository.findAll()
    }

    @GetMapping("/{id}")
    suspend fun geById(@PathVariable id: String): Product {
        return repository.findById(id) ?: throw ResourceException.notFound("Product")
    }

    @GetMapping("/search/{query}")
    suspend fun search(@PathVariable query: String): Flow<Product> {
        val criteria = TextCriteria.forDefaultLanguage().matching(query)
        return repository.findAllBy(criteria)
    }

    @GetMapping("/category/{categoryId}")
    suspend fun getByCategoryId(@PathVariable categoryId: String): Flow<Product> {
        return repository.findAllByCategoryIds(categoryId)
    }

    @GetMapping("/brand/{brandId}")
    suspend fun getByBrandId(@PathVariable brandId: String): Flow<Product> {
        return repository.findAllByBrandId(brandId)
    }

    @GetMapping("/filter")
    suspend fun filterByCategoryAndBrand(
        @RequestParam categoryId: String?,
        @RequestParam brandId: String?
    ): Flow<Product> {
        return repository.filterIfPresent(categoryId, brandId)
    }
}

