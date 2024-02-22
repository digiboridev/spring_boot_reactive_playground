package com.digiboridev.rxpg.controller

import com.digiboridev.rxpg.core.exceptions.ResourceException
import com.digiboridev.rxpg.core.security.AppAuthentication
import com.digiboridev.rxpg.data.model.Category
import com.digiboridev.rxpg.data.repository.CategoriesRepository
import com.digiboridev.rxpg.data.valueObject.Role
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import kotlinx.coroutines.flow.Flow
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


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
        return repository.findById(id) ?: throw ResourceException.notFound("Category")
    }

    @GetMapping("/{parentId}/")
    suspend fun getByParentId(@PathVariable parentId: String): Flow<Category> {
        return repository.findAllByParentId(parentId)
    }

    @PostMapping("/")
    @PreAuthorize("authenticated")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun createCategory(@RequestBody @Valid category: NewCategoryRequest,auth: AppAuthentication): Category {
        if (auth.role != Role.ADMIN) throw ResourceException.forbidden("category creation")

        val newCategory = Category(
            name = category.name!!,
            description = category.description!!,
            parentId = category.parentId!!,
            image = category.image!!
        )

        return repository.save(newCategory)
    }

    @PatchMapping("/{id}")
    @PreAuthorize("authenticated")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun updateCategory(
        @PathVariable id: String,
        @RequestBody @Valid category: UpdateCategoryRequest, auth: AppAuthentication
    ): Category {
        if (auth.role != Role.ADMIN) throw ResourceException.forbidden("category update")

        val categoryToUpdate = repository.findById(id) ?: throw ResourceException.notFound("Category")
        val editedCategory = categoryToUpdate.copy(
            name = category.name ?: categoryToUpdate.name,
            description = category.description ?: categoryToUpdate.description,
            parentId = category.parentId ?: categoryToUpdate.parentId,
            image = category.image ?: categoryToUpdate.image
        )

        return repository.save(editedCategory)
    }




    data class NewCategoryRequest(
        @field:NotBlank
        val name: String?,
        @field:NotBlank
        val description: String?,
        @field:NotBlank
        val parentId: String?,
        @field:NotBlank
        val image : String?,
    )

    data class UpdateCategoryRequest(
        val name: String?,
        val description: String?,
        val parentId: String?,
        val image : String?,
    )

}

