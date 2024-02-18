package com.digiboridev.rxpg.service


import com.digiboridev.rxpg.data.repository.BrandsRepository
import com.digiboridev.rxpg.data.repository.CategoriesRepository
import com.digiboridev.rxpg.data.repository.ProductsRepository
import org.springframework.stereotype.Service


@Service
class ProductsService(
    val productsRepository: ProductsRepository,
    val brandsRepository: BrandsRepository,
    val categoriesRepository: CategoriesRepository
) {

}

