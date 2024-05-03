package com.example.ecommercemobile.store.domain.model
import com.example.ecommercemobile.store.domain.model.core.ProductCategory

data class MetadataProductCategory(
    val page: Int? = null,
    val total: Int? = null,
    val totalPage: Int? = null,
    val products: ProductCategory
)