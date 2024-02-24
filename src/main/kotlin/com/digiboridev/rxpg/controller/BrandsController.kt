package com.digiboridev.rxpg.controller

import com.digiboridev.rxpg.core.exceptions.ResourceException
import com.digiboridev.rxpg.core.security.AppAuthentication
import com.digiboridev.rxpg.data.dto.BrandData
import com.digiboridev.rxpg.data.model.Brand
import com.digiboridev.rxpg.data.repository.BrandsRepository
import com.digiboridev.rxpg.data.valueObject.Role
import com.digiboridev.rxpg.service.BrandsService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@Tag(name = "Brands", description = "Brands endpoints")
@RestController
@RequestMapping("/api/brands")
class BrandsController(
    val repository: BrandsRepository,
    val service: BrandsService
) {

    @GetMapping("/")
    suspend fun getAll(): Flow<BrandData> {
        return repository.findAllBrandDataBy()
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: String): BrandData {
        return repository.findBrandDataById(id) ?: throw ResourceException.notFound("Brand")
    }

    @GetMapping("/fulltext-search/{query}")
    suspend fun fullTextSearch(@PathVariable query: String): Flow<BrandData> {
        return service.fullTextSearch(query)
    }

    @GetMapping("/vector-search/{query}")
    suspend fun vectorSearch(@PathVariable query: String): Flow<BrandData> {
        return service.vectorTextSearch(query)
    }

    @PostMapping("/")
    @PreAuthorize("authenticated")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun createBrand(@RequestBody @Valid brand: NewBrandRequest, auth: AppAuthentication): Brand {
        if (auth.role != Role.ADMIN) throw ResourceException.forbidden("brand creation")

        val newBrand = Brand(name = brand.name!!, description = brand.description!!)
        return repository.save(newBrand)
    }


    @PatchMapping("/{id}")
    @PreAuthorize("authenticated")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun updateBrand(
        @PathVariable id: String,
        @RequestBody @Valid brand: UpdateBrandRequest, auth: AppAuthentication
    ): Brand {
        if (auth.role != Role.ADMIN) throw ResourceException.forbidden("brand update")

        val brandToUpdate = repository.findById(id) ?: throw ResourceException.notFound("Brand")
        val editedBrand = brandToUpdate.copy(
            name = brand.name ?: brandToUpdate.name,
            description = brand.description ?: brandToUpdate.description
        )
        return repository.save(editedBrand)
    }


    data class NewBrandRequest(
        @field:NotBlank
        val name: String?,
        @field:NotBlank
        val description: String?,
    )

    data class UpdateBrandRequest(
        val name: String?,
        val description: String?,
    )
}

