package com.digiboridev.rxpg.valueObject

data class PriceRange(
    /// The amount of the price in the smallest unit of the currency (e.g. cents for USD)
    val min: Int,
    val max: Int,
    /// The currency of the price
    val currency: Currency
)