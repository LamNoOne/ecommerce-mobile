package com.example.ecommercemobile.store.domain.model

import com.example.ecommercemobile.store.domain.model.core.Product

data class MetadataProducts(
    val page: Int ?= null,
    val total: Int ?= null,
    val totalPage: Int ?= null,
    val products: List<Product>
)