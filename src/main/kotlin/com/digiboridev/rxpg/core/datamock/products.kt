package com.digiboridev.rxpg.core.datamock

import com.digiboridev.rxpg.data.model.*
import com.digiboridev.rxpg.data.repository.BrandsRepository
import com.digiboridev.rxpg.data.repository.CategoriesRepository
import com.digiboridev.rxpg.data.repository.ProductsRepository
import com.digiboridev.rxpg.data.valueObject.Currency
import com.digiboridev.rxpg.data.valueObject.Price
import com.digiboridev.rxpg.data.valueObject.ProductAvailability
import com.digiboridev.rxpg.service.EmbeddingsService
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
    val productsRepository: ProductsRepository,
    val embeddingsService: EmbeddingsService
) {

    @OptIn(DelicateCoroutinesApi::class)
    @PostConstruct
    fun postConstruct() {
        println("ProductsDataMock postConstruct...")
        GlobalScope.launch {
//            println("ProductsDataMock initializing...")
//            fillData(brandsRepository, categoriesRepository, productsRepository, embeddingsService)
//            println("ProductsDataMock initialized")
        }
    }
}

suspend fun fillData(
    brandsRepository: BrandsRepository,
    categoriesRepository: CategoriesRepository,
    productsRepository: ProductsRepository,
    embeddingsService: EmbeddingsService
) {
    brandsRepository.deleteAll()
    categoriesRepository.deleteAll()
    productsRepository.deleteAll()

    val brands = mutableListOf<Brand>()
    brandsRepository.saveAll(
        listOf(
            Brand(name = "Aurora", description = "Best music company in the world"),
            Brand(name = "Vertex", description = "Leading provider of innovative software solutions"),
            Brand(name = "Eclipse", description = "Premium eyewear brand known for its sleek designs"),
            Brand(name = "NovaTech", description = "Cutting-edge technology company specializing in AI and robotics"),
            Brand(name = "TerraFirma", description = "Environmentally conscious outdoor gear manufacturer"),
            Brand(name = "Luminous", description = "Luxury lighting fixtures for homes and businesses"),
            Brand(name = "VitaLife", description = "Health and wellness brand offering organic supplements"),
            Brand(name = "Horizon", description = "Travel agency providing unforgettable adventures across the globe"),
            Brand(name = "Pulse", description = "Innovative fitness equipment designed for peak performance"),
            Brand(name = "Harmony", description = "Natural skincare products made with organic ingredients"),
            Brand(name = "Nimbus", description = "High-performance bicycles for cycling enthusiasts"),
            Brand(name = "Serenity", description = "Premium mattress brand committed to quality sleep"),
            Brand(name = "Zenith", description = "Leading watchmaker crafting precision timepieces since 1865"),
            Brand(name = "Cascade", description = "Eco-friendly cleaning products for a sustainable lifestyle"),
            Brand(name = "Appendix", description = "Outdoor gear brand specializing in rugged equipment for adventurers"),
            Brand(name = "Empire", description = "Fashion label known for its elegant and timeless designs"),
            Brand(name = "Sapphire", description = "Fine jewelry brand renowned for its exquisite gemstones"),
            Brand(name = "Polaris", description = "Trusted manufacturer of recreational vehicles and utility equipment"),
            Brand(name = "Equinox", description = "Premium fitness club offering personalized training and amenities"),
            Brand(name = "Innovated", description = "Innovative technology company revolutionizing the digital landscape"),
            Brand(name = "Crimson", description = "Boutique winery specializing in small-batch artisanal wines"),
            Brand(name = "Tranquil", description = "Luxury spa retreat offering holistic wellness experiences"),
            Brand(name = "Vanguard", description = "Progressive fashion brand pushing the boundaries of style"),
            Brand(name = "Phoenix", description = "Energy drink company fueling peak performance and vitality"),
            Brand(name = "Mystique", description = "Exotic fragrance line capturing the essence of distant lands"),
            Brand(name = "Aether", description = "High-end audio equipment delivering unparalleled sound quality"),
            Brand(name = "Veritas", description = "Educational platform offering online courses in various disciplines"),
            Brand(name = "Amber", description = "Artisanal candle maker using natural ingredients and essential oils"),
            Brand(name = "Quasar", description = "Leading-edge technology firm specializing in quantum computing"),
            Brand(name = "Onyx", description = "Luxury car manufacturer known for its sleek and powerful vehicles"),
            Brand(name = "Celestial", description = "Cosmetics brand inspired by the beauty of the night sky"),
            Brand(name = "Meridian", description = "Sustainable fashion label committed to ethical production practices"),
            Brand(name = "Apex", description = "Cutting-edge gaming peripherals for competitive gamers"),
            Brand(name = "Orbit", description = "Space tourism company offering trips to the outer reaches of space"),
            Brand(name = "Cascades", description = "Artisanal chocolate maker using only the finest cocoa beans"),
            Brand(name = "Vortex", description = "Leading provider of high-performance gaming PCs and accessories"),
            Brand(name = "Solaris", description = "Renewable energy company harnessing the power of the sun"),
            Brand(name = "Fusion", description = "Blend of Eastern and Western cuisine in a modern dining experience"),
            Brand(name = "Lunar", description = "Luxury travel agency specializing in exclusive lunar excursions"),
            Brand(name = "Synergy", description = "Collaborative workspace provider fostering innovation and creativity"),
            Brand(name = "Elysium", description = "Luxury resort chain offering unparalleled hospitality and relaxation"),
            Brand(name = "Chronos", description = "Swiss watchmaker renowned for its precision and craftsmanship"),
            Brand(name = "Omega", description = "Leading provider of nutritional supplements for optimal health"),
            Brand(name = "Renaissance", description = "Artisanal bakery creating delectable pastries and breads"),
            Brand(name = "Tempest", description = "Premium outdoor apparel brand for adventurers and explorers"),
            Brand(name = "Fahrenheit", description = "Contemporary art gallery showcasing cutting-edge works"),
            Brand(name = "Venus", description = "Luxury skincare brand using rare and exotic botanicals"),
            Brand(name = "Eclipse Gen", description = "Exclusive nightclub offering unparalleled nightlife experiences"),
            Brand(name = "Spectra", description = "Provider of high-speed internet and telecommunications services"),
            Brand(name = "Mercury", description = "Luxury automotive brand redefining the concept of driving"),
            Brand(name = "Haven", description = "Retreat center offering transformative experiences in nature"),
            Brand(name = "Nebula", description = "Artistic lighting installations inspired by the cosmos"),
            Brand(name = "Epoch", description = "Luxury real estate developer creating iconic properties worldwide"),
            Brand(name = "Pantheon", description = "Couture fashion house renowned for its opulent designs"),
            Brand(name = "Savant", description = "Provider of cutting-edge home automation systems for modern living")
        )
    ).toCollection(brands)

    brands.forEach {
        val embeddings = embeddingsService.getEmbeddings(it.name + " " + it.description)
        brandsRepository.save(it.copy(vec = embeddings))
    }



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
                brandId = brands.first { it.name == "Omega" }.id,
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