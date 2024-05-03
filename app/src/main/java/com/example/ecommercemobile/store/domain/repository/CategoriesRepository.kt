package com.example.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.example.ecommercemobile.core.NetworkError
import com.example.ecommercemobile.store.domain.model.MetadataCategories
import com.example.ecommercemobile.store.domain.model.Response

interface CategoriesRepository {
    suspend fun getCategories(): Either<NetworkError, Response<MetadataCategories>>
}