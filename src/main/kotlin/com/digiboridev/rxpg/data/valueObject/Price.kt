package com.digiboridev.rxpg.data.valueObject

data class Price(
    /// The amount of the price in the smallest unit of the currency (e.g. cents for USD)
    val amount: Int,
    /// The currency of the price
    val currency: Currency
)

