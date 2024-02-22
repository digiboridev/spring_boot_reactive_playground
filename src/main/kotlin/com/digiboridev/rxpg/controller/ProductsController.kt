package com.digiboridev.rxpg.controller

import com.digiboridev.rxpg.core.exceptions.ResourceException
import com.digiboridev.rxpg.data.dto.NewProductRequest
import com.digiboridev.rxpg.data.dto.UpdateProductRequest
import com.digiboridev.rxpg.data.model.Product
import com.digiboridev.rxpg.data.repository.ProductsRepository
import com.digiboridev.rxpg.service.ProductsService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@Tag(name = "Products", description = "Products endpoints")
@RestController
@RequestMapping("/api/products")
class ProductsController(val repository: ProductsRepository, val service: ProductsService) {

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

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun createProduct(@RequestBody @Valid product: NewProductRequest): Product {
        return service.createProduct(product)
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun updateProduct(@PathVariable id: String, @RequestBody product: UpdateProductRequest): Product {
        return service.updateProduct(id, product)
    }
}

