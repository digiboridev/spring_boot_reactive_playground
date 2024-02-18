package com.digiboridev.rxpg.service

//import com.digiboridev.rxpg.model.Category
import com.digiboridev.rxpg.data.model.Brand
import com.digiboridev.rxpg.data.model.Category
import com.digiboridev.rxpg.data.model.Product
import com.digiboridev.rxpg.data.repository.BrandsRepository
import com.digiboridev.rxpg.data.repository.CategoriesRepository
import com.digiboridev.rxpg.data.repository.ProductsRepository
import com.digiboridev.rxpg.data.valueObject.Currency
import com.digiboridev.rxpg.data.valueObject.Price
import com.digiboridev.rxpg.data.valueObject.PriceRange
import com.digiboridev.rxpg.data.valueObject.ProductAvailability
import org.springframework.stereotype.Service


@Service
class ProductsService(
    val productsRepository: ProductsRepository,
    val brandsRepository: BrandsRepository,
    val categoriesRepository: CategoriesRepository
) {

//
//    @OptIn(DelicateCoroutinesApi::class)
//    @PostConstruct
//    fun init() {
//        println("ProductsService initializing...")
//        CoroutineScope(GlobalScope.coroutineContext).launch {
//            fillData(brandsRepository, categoriesRepository, productsRepository)
//        }
//        println("ProductsService initialized")
//
//    }
}


suspend fun fillData(
    brandsRepository: BrandsRepository,
    categoriesRepository: CategoriesRepository,
    productsRepository: ProductsRepository
) {
    brandsRepository.deleteAll()
    categoriesRepository.deleteAll()
    productsRepository.deleteAll()

    val brand1 = brandsRepository.save(Brand(name = "Brand 1", description = "Brand 1 description"))
    val brand2 = brandsRepository.save(Brand(name = "Brand 2", description = "Brand 2 description"))
    val brand3 = brandsRepository.save(Brand(name = "Brand 3", description = "Brand 3 description"))
    val brand4 = brandsRepository.save(Brand(name = "Brand 4", description = "Brand 4 description"))
    val brand5 = brandsRepository.save(Brand(name = "Brand 5", description = "Brand 5 description"))
    val brand6 = brandsRepository.save(Brand(name = "Brand 6", description = "Brand 6 description"))
    val brand7 = brandsRepository.save(Brand(name = "Brand 7", description = "Brand 7 description"))
    val brand8 = brandsRepository.save(Brand(name = "Brand 8", description = "Brand 8 description"))
    val brand9 = brandsRepository.save(Brand(name = "Brand 9", description = "Brand 9 description"))
    val brand10 = brandsRepository.save(Brand(name = "Brand 10", description = "Brand 10 description"))


    val cat1 = categoriesRepository.save(Category(name = "Top Category 1", description = "Top Category 1 description"))
    val cat2 = categoriesRepository.save(Category(name = "Top Category 2", description = "Top Category 2 description"))
    val cat3 = categoriesRepository.save(Category(name = "Top Category 3", description = "Top Category 3 description"))
    val cat4 = categoriesRepository.save(Category(name = "Top Category 4", description = "Top Category 4 description"))
    val cat5 = categoriesRepository.save(Category(name = "Top Category 5", description = "Top Category 5 description"))
    val cat6 = categoriesRepository.save(Category(name = "Top Category 6", description = "Top Category 6 description"))

    val subCat1 = categoriesRepository.save(
        Category(
            name = "Sub Category 1",
            description = "Sub Category 1 description",
            parentId = cat1.id,
            level = 1
        )
    )
    val subCat2 = categoriesRepository.save(
        Category(
            name = "Sub Category 2",
            description = "Sub Category 2 description",
            parentId = cat1.id,
            level = 1
        )
    )
    val subCat3 = categoriesRepository.save(
        Category(
            name = "Sub Category 3",
            description = "Sub Category 3 description",
            parentId = cat1.id,
            level = 1
        )
    )
    val subCat4 = categoriesRepository.save(
        Category(
            name = "Sub Category 4",
            description = "Sub Category 4 description",
            parentId = cat1.id,
            level = 1
        )
    )


    val productEntity = productsRepository.save(
        Product(
            name = "Product 1",
            description = "Product 1 description",
            brandId = brand1.id,
            categoryIds = listOf(cat1.id,subCat4.id),
            priceRange = PriceRange(100, 200, Currency.USD),
            availability = ProductAvailability.AVAILABLE,
            models = listOf(
                Product.Model(
                    name = "Model 1",
                    price = Price(100, Currency.USD),
                    sku = "SKU 1",
                    availability = ProductAvailability.AVAILABLE,
                    properties = listOf(
                        Product.ModelProperty("Color", "color_black", "Black"),
                        Product.ModelProperty("Memory", "memory_128gb", "128GB")
                    )
                ),
                Product.Model(
                    name = "Model 2",
                    price = Price(200, Currency.USD),
                    sku = "SKU 2",
                    availability = ProductAvailability.AVAILABLE,
                    properties = listOf(
                        Product.ModelProperty("Color", "color_white", "White"),
                        Product.ModelProperty("Memory", "memory_128gb", "128GB")
                    )
                ),
                Product.Model(
                    name = "Model 3",
                    price = Price(300, Currency.USD),
                    sku = "SKU 3",
                    availability = ProductAvailability.OUT_OF_STOCK,
                    properties = listOf(
                        Product.ModelProperty("Color", "color_black", "Black"),
                        Product.ModelProperty("Memory", "memory_256gb", "256GB")
                    )
                ),
                Product.Model(
                    name = "Model 3",
                    price = Price(300, Currency.USD),
                    sku = "SKU 3",
                    availability = ProductAvailability.AVAILABLE,
                    properties = listOf(
                        Product.ModelProperty("Color", "color_white", "White"),
                        Product.ModelProperty("Memory", "memory_256gb", "256GB")
                    )
                )
            ),
            additions = listOf(
                Product.Addition(
                    name = "Warranty",
                    type = Product.Addition.Type.WARRANTY,
                    maxQuantity = 1,
                    price = Price(50, Currency.USD)
                ),
                Product.Addition(
                    name = "Case",
                    type = Product.Addition.Type.ACCESSORY,
                    maxQuantity = 1,
                    price = Price(10, Currency.USD)
                ),
                Product.Addition(
                    name = "Spare battery",
                    type = Product.Addition.Type.SPARE,
                    maxQuantity = 5,
                    price = Price(5, Currency.USD)
                )
            )
        )
    )


    val brands = mutableListOf<Brand>()
    brandsRepository.findAll().collect { brands.add(it) }

    val categories = mutableListOf<Category>()
    categoriesRepository.findAll().collect { categories.add(it) }

    val products = mutableListOf<Product>()
    productsRepository.findAll().collect { products.add(it) }


    println("Brands: $brands")
    println("Categories: $categories")
    println("Products: $products")
}