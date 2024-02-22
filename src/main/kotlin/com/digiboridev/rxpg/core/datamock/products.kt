package com.digiboridev.rxpg.core.datamock

import com.digiboridev.rxpg.data.model.*
import com.digiboridev.rxpg.data.repository.BrandsRepository
import com.digiboridev.rxpg.data.repository.CategoriesRepository
import com.digiboridev.rxpg.data.repository.ProductsRepository
import com.digiboridev.rxpg.data.valueObject.Currency
import com.digiboridev.rxpg.data.valueObject.Price
import com.digiboridev.rxpg.data.valueObject.ProductAvailability
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component


@Component
class ProductsDataMock(
    val brandsRepository: BrandsRepository,
    val categoriesRepository: CategoriesRepository,
    val productsRepository: ProductsRepository
) {

    @OptIn(DelicateCoroutinesApi::class)
    @PostConstruct
    fun postConstruct() {
        println("ProductsDataMock postConstruct...")
        GlobalScope.launch {
//            println("ProductsDataMock initializing...")
//            fillData(brandsRepository, categoriesRepository, productsRepository)
//            println("ProductsDataMock initialized")
        }
    }
}

suspend fun fillData(
    brandsRepository: BrandsRepository,
    categoriesRepository: CategoriesRepository,
    productsRepository: ProductsRepository
) {
    brandsRepository.deleteAll()
    categoriesRepository.deleteAll()
    productsRepository.deleteAll()

    val brands = mutableListOf<Brand>()
    brandsRepository.saveAll(
        listOf(
            Brand(name = "Brand 1", description = "Brand 1 description"),
            Brand(name = "Brand 2", description = "Brand 2 description"),
            Brand(name = "Brand 3", description = "Brand 3 description"),
            Brand(name = "Brand 4", description = "Brand 4 description"),
            Brand(name = "Brand 5", description = "Brand 5 description"),
            Brand(name = "Brand 6", description = "Brand 6 description"),
            Brand(name = "Brand 7", description = "Brand 7 description"),
            Brand(name = "Brand 8", description = "Brand 8 description"),
        )
    ).toCollection(brands)


    val rootCategories = mutableListOf<Category>()

    categoriesRepository.saveAll(
        listOf(
            Category(name = "Top Category 1", description = "Top Category 1 description"),
            Category(name = "Top Category 2", description = "Top Category 2 description"),
            Category(name = "Top Category 3", description = "Top Category 3 description"),
            Category(name = "Top Category 4", description = "Top Category 4 description"),
            Category(name = "Top Category 5", description = "Top Category 5 description"),
            Category(name = "Top Category 6", description = "Top Category 6 description"),
        )
    ).toCollection(rootCategories)

    val subCategories = mutableListOf<Category>()
    categoriesRepository.saveAll(
        listOf(
            Category(
                name = "Sub Category 1",
                description = "Sub Category 1 description",
                parentId = rootCategories[0].id
            ),
            Category(
                name = "Sub Category 2",
                description = "Sub Category 2 description",
                parentId = rootCategories[0].id
            ),
            Category(
                name = "Sub Category 3",
                description = "Sub Category 3 description",
                parentId = rootCategories[0].id
            ),
            Category(
                name = "Sub Category 4",
                description = "Sub Category 4 description",
                parentId = rootCategories[0].id
            ),
            Category(
                name = "Sub Category 5",
                description = "Sub Category 5 description",
                parentId = rootCategories[0].id
            ),
            Category(
                name = "Sub Category 6",
                description = "Sub Category 6 description",
                parentId = rootCategories[0].id
            ),
        )
    ).toCollection(subCategories)

    val products = mutableListOf<Product>()

    productsRepository.saveAll(
        listOf(
            Product(
                name = "Product 1",
                description = "Product 1 description",
                brandId = brands.first { it.name == "Brand 1" }.id,
                categoryIds = listOf(
                    subCategories.first { it.name == "Sub Category 1" }.id,
                    subCategories.first { it.name == "Sub Category 2" }.id
                ),
                models = listOf(
                    ProductModel(
                        name = "Model 1",
                        price = Price(100, Currency.USD),
                        sku = "SKU 1",
                        availability = ProductAvailability.AVAILABLE,
                        properties = listOf(
                            ModelProperty("Color", "color_black", "Black"),
                            ModelProperty("Memory", "memory_128gb", "128GB")
                        )
                    ),
                    ProductModel(
                        name = "Model 2",
                        price = Price(200, Currency.USD),
                        sku = "SKU 2",
                        availability = ProductAvailability.AVAILABLE,
                        properties = listOf(
                            ModelProperty("Color", "color_white", "White"),
                            ModelProperty("Memory", "memory_128gb", "128GB")
                        )
                    ),
                    ProductModel(
                        name = "Model 3",
                        price = Price(300, Currency.USD),
                        sku = "SKU 3",
                        availability = ProductAvailability.OUT_OF_STOCK,
                        properties = listOf(
                            ModelProperty("Color", "color_black", "Black"),
                            ModelProperty("Memory", "memory_256gb", "256GB")
                        )
                    ),
                    ProductModel(
                        name = "Model 3",
                        price = Price(300, Currency.USD),
                        sku = "SKU 3",
                        availability = ProductAvailability.AVAILABLE,
                        properties = listOf(
                            ModelProperty("Color", "color_white", "White"),
                            ModelProperty("Memory", "memory_256gb", "256GB")
                        )
                    )
                ),
                additions = listOf(
                    ProductAddition(
                        name = "Warranty",
                        type = ProductAddition.Type.WARRANTY,
                        maxQuantity = 1,
                        price = Price(50, Currency.USD)
                    ),
                    ProductAddition(
                        name = "Case",
                        type = ProductAddition.Type.ACCESSORY,
                        maxQuantity = 1,
                        price = Price(10, Currency.USD)
                    ),
                    ProductAddition(
                        name = "Spare battery",
                        type = ProductAddition.Type.SPARE,
                        maxQuantity = 5,
                        price = Price(5, Currency.USD)
                    )
                )

            ),
        )

    ).collect { products.add(it) }

    brandsRepository.findAll().collect { println(it) }
    categoriesRepository.findAll().collect { println(it) }
    productsRepository.findAll().collect { println(it) }
}