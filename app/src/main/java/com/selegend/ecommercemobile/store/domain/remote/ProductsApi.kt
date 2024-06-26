package com.selegend.ecommercemobile.store.domain.remote

import com.selegend.ecommercemobile.store.domain.model.MetadataProduct
import com.selegend.ecommercemobile.store.domain.model.MetadataProductCategory
import com.selegend.ecommercemobile.store.domain.model.MetadataProducts
import com.selegend.ecommercemobile.store.domain.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {

    // Call api theo categoryId
    @GET("products")
    suspend fun getProducts(
        @Query("name") name: String?,
        @Query("categoryId") categoryId: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sortBy") sortBy: String?,
        @Query("order") order: String?
    ): Response<MetadataProducts>

    // Call api theo name
    @GET("products")
    suspend fun searchProduct(
        @Query("name") name: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sortBy") sortBy: String?,
        @Query("order") order: String?
    ): Response<MetadataProducts>

    @GET("products/get-product")
    suspend fun getProductById(
        @Query("id") id: Int
    ): Response<MetadataProduct>

    @GET("categories/get-products")
    suspend fun getProductsByCategory(
        @Query("id") id: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<MetadataProductCategory>
}