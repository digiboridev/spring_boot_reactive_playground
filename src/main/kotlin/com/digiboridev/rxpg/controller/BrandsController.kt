package com.digiboridev.rxpg.controller

import com.digiboridev.rxpg.core.exceptions.ResourceException
import com.digiboridev.rxpg.core.security.AppAuthentication
import com.digiboridev.rxpg.data.model.Brand
import com.digiboridev.rxpg.data.repository.BrandsRepository
import com.digiboridev.rxpg.data.valueObject.Role
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

