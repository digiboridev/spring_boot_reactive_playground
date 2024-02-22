package com.digiboridev.rxpg.data.dto

import com.digiboridev.rxpg.data.model.ProductAddition
import com.digiboridev.rxpg.data.model.ProductModel
import com.digiboridev.rxpg.data.model.ProductState
import org.springframework.validation.annotation.Validated

data class UpdateProductRequest(
    val id: String?,
    val name: String?,
    val description: String?,
    val brandId: String?,
    val categoryIds: List<String>?,
    val images: List<String>?,
    val models: List<ProductModel>?,
    val additions: List<ProductAddition>?,
    val state: ProductState?
)