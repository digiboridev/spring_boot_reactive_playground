package com.digiboridev.rxpg.data.dto

import com.digiboridev.rxpg.data.model.ProductAddition
import com.digiboridev.rxpg.data.model.ProductModel
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty


data class NewProductRequest(
    @field: NotBlank
    val name: String?,
    @field: NotBlank
    val description: String?,

    @field: NotBlank
    val brandId: String?,
    @field: NotEmpty
    val categoryIds: List<String>?,
    @field: NotEmpty
    val images: List<String>?,

    @field: NotEmpty
    val models: List<ProductModel>?,
    @field: NotEmpty
    val additions: List<ProductAddition>?,
)