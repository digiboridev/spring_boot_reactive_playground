package com.digiboridev.rxpg.data.model

import com.digiboridev.rxpg.data.valueObject.Price
import com.digiboridev.rxpg.data.valueObject.ProductAvailability

// Product model represents a specific product variation with different price, sku, and availability
// For example, Iphone 12 Pro 128GB, Iphone 12 Pro 256GB, etc.
data class ProductModel(
    val name: String,
    val price: Price,
    val sku: String,
    val availability: ProductAvailability,
    val properties: List<ModelProperty> = emptyList(),
)

