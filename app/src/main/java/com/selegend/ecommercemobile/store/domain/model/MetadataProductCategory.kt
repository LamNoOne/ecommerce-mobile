package com.selegend.ecommercemobile.store.domain.model
import com.selegend.ecommercemobile.store.domain.model.core.products.ProductCategory

data class MetadataProductCategory(
    val page: Int? = null,
    val total: Int? = null,
    val totalPage: Int? = null,
    val products: ProductCategory
)