package com.digiboridev.rxpg.data.model

import com.digiboridev.rxpg.data.valueObject.AverageRating
import com.digiboridev.rxpg.data.valueObject.PriceRange
import com.digiboridev.rxpg.data.valueObject.ProductAvailability
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant


@Document(collection = "products")
@CompoundIndexes(
    CompoundIndex(name = "brand_category", def = "{'brandId': 1, 'categoryIds': 1}"),
    CompoundIndex(name = "brand_category_state", def = "{'brandId': 1, 'categoryIds': 1, 'state': 1}"),
    CompoundIndex(name = "state_updatedAt", def = "{'state': 1, 'updatedAt': 1}"),
    CompoundIndex(name = "availability_updatedAt", def = "{'availability': 1, 'updatedAt': 1}")
)
data class Product(
    @Id
    val id: String = ObjectId().toString(),

    @TextIndexed
    val name: String,
    @TextIndexed
    val description: String,

    val brandId: String? = null,
    val categoryIds: List<String>,
    val images: List<String> = emptyList(),
    val averageRating: AverageRating = AverageRating(),

    val models: List<ProductModel> = emptyList(),
    val additions: List<ProductAddition> = emptyList(),

    val state: ProductState = ProductState.DRAFT,

    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val updatedAt: Instant = Instant.now(),
    @Version
    val version: Long = 0
)







