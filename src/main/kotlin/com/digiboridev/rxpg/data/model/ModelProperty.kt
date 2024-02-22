package com.digiboridev.rxpg.data.model

// Represents the property by which models differ from each other.
// It can be grouped by name to display as options, so the matrix of properties forms result model
// For example, size, color, weight, memory etc.
data class ModelProperty(
    val groupName: String,
    val valueCode: String,
    val valueText: String
)