package com.digiboridev.rxpg.data.model

import com.digiboridev.rxpg.data.valueObject.AverageRating
import com.digiboridev.rxpg.data.valueObject.Price
import com.digiboridev.rxpg.data.valueObject.PriceRange
import com.digiboridev.rxpg.data.valueObject.ProductAvailability
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant


@Document(collection = "products")
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

    // Price range between models
    val priceRange: PriceRange,
    // Resulting availability of the product based on the models
    val availability: ProductAvailability = ProductAvailability.AVAILABLE,

    val models: List<Model> = emptyList(),
    val additions: List<Addition> = emptyList(),

    val state: State = State.DRAFT,

    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val updatedAt: Instant = Instant.now(),
    @Version
    val version: Long = 0
) {
    // Product lifecycle state
    enum class State {
        DRAFT, VISIBLE, DISABLED, ARCHIVED
    }

    // Product model represents a specific product variation with different price, sku, and availability
    // For example, Iphone 12 Pro 128GB, Iphone 12 Pro 256GB, etc.
    data class Model(
        val name: String,
        val price: Price,
        val sku: String,
        val availability: ProductAvailability,
        val properties: List<ModelProperty> = emptyList(),
    )

    // Represents the property by which models differ from each other.
    // It can be grouped by name to display as options, so the matrix of properties forms result model
    // For example, size, color, weight, memory etc.
    data class ModelProperty(
        val groupName: String,
        val valueCode: String,
        val valueText: String
    )

    // Represents services or sub products that can be added or not to the main product
    // For example warranty, insurance, cheese, sugar, etc.
    data class Addition(
        val name: String,
        val type: Type,
        val maxQuantity: Int = 1,
        val price: Price,
    ) {
        // Type of the addition, to be used for filtering, grouping and displaying
        enum class Type { WARRANTY, INSURANCE, ACCESSORY, PACKAGE, CONSUMABLE, SPARE, OTHER }
    }
}











