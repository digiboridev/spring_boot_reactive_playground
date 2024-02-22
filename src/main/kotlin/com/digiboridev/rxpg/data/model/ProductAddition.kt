package com.digiboridev.rxpg.data.model

import com.digiboridev.rxpg.data.valueObject.Price

// Represents services or sub products that can be added or not to the main product
// For example warranty, insurance, cheese, sugar, etc.
data class ProductAddition(
    val name: String,
    val type: Type,
    val maxQuantity: Int = 1,
    val price: Price,
) {
    // Type of the addition, to be used for filtering, grouping and displaying
    enum class Type { WARRANTY, INSURANCE, ACCESSORY, PACKAGE, CONSUMABLE, SPARE, OTHER }
}