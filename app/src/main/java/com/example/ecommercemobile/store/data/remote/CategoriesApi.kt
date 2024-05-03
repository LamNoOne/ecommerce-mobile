package com.example.ecommercemobile.store.data.remote

import com.example.ecommercemobile.store.domain.model.MetadataCategories
import com.example.ecommercemobile.store.domain.model.Response
import retrofit2.http.GET

interface CategoriesApi {
    @GET("categories")
    suspend fun getCategories(): Response<MetadataCategories>
}