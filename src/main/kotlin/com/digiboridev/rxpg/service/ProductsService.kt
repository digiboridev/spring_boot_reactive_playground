package com.digiboridev.rxpg.service


import com.digiboridev.rxpg.core.exceptions.ResourceException
import com.digiboridev.rxpg.data.dto.NewProductRequest
import com.digiboridev.rxpg.data.dto.UpdateProductRequest
import com.digiboridev.rxpg.data.model.Product
import com.digiboridev.rxpg.data.model.ProductModel
import com.digiboridev.rxpg.data.model.ProductState
import com.digiboridev.rxpg.data.repository.BrandsRepository
import com.digiboridev.rxpg.data.repository.CategoriesRepository
import com.digiboridev.rxpg.data.repository.ProductsRepository
import com.digiboridev.rxpg.data.valueObject.AverageRating
import com.digiboridev.rxpg.data.valueObject.PriceRange
import com.digiboridev.rxpg.data.valueObject.ProductAvailability
import org.springframework.stereotype.Service


@Service
class ProductsService(
    val productsRepository: ProductsRepository,
    val brandsRepository: BrandsRepository,
    val categoriesRepository: CategoriesRepository
) {
    suspend fun createProduct(product: NewProductRequest): Product {
        val models = product.models!!

        val newProduct = Product(
            name = product.name!!,
            description = product.description!!,
            brandId = product.brandId,
            categoryIds = product.categoryIds!!,
            images = product.images!!,
            averageRating = AverageRating(),
            models = models,
            additions = product.additions!!,
            state = ProductState.DRAFT
        )

        return productsRepository.save(newProduct)
    }

    suspend fun updateProduct(id: String, product: UpdateProductRequest): Product {
        val productToUpdate = productsRepository.findById(id) ?: throw ResourceException.notFound("Product")

        val models = product.models ?: productToUpdate.models

        val editedProduct = productToUpdate.copy(
            name = product.name ?: productToUpdate.name,
            description = product.description ?: productToUpdate.description,
            brandId = product.brandId ?: productToUpdate.brandId,
            categoryIds = product.categoryIds ?: productToUpdate.categoryIds,
            images = product.images ?: productToUpdate.images,
            models = models,
            additions = product.additions ?: productToUpdate.additions,
            state = product.state ?: productToUpdate.state
        )

        return productsRepository.save(editedProduct)
    }

    // availability of the whole product based on its models
    private fun productAvailability(models: List<ProductModel>): ProductAvailability {
        val hasAvailable = models.any { it.availability == ProductAvailability.AVAILABLE }
        val hasOutOfStock = models.any { it.availability == ProductAvailability.OUT_OF_STOCK }

        if (hasAvailable) return ProductAvailability.AVAILABLE
        if (hasOutOfStock) return ProductAvailability.OUT_OF_STOCK
        return ProductAvailability.DISCONTINUED
    }

    // price range of the whole product based on its models
    private fun priceRange(models: List<ProductModel>): PriceRange {
        val minPrice = models.minOf { it.price.amount }
        val maxPrice = models.maxOf { it.price.amount }
        val currency = models.first().price.currency
        return PriceRange(minPrice, maxPrice, currency)
    }
}

