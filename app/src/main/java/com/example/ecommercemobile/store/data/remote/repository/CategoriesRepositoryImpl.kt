package com.example.ecommercemobile.store.data.remote.repository

import arrow.core.Either
import com.example.ecommercemobile.core.NetworkError
import com.example.ecommercemobile.store.data.mapper.toNetworkError
import com.example.ecommercemobile.store.data.remote.CategoriesApi
import com.example.ecommercemobile.store.domain.model.MetadataCategories
import com.example.ecommercemobile.store.domain.model.Response
import com.example.ecommercemobile.store.domain.repository.CategoriesRepository
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val categoriesApi: CategoriesApi
): CategoriesRepository {
    override suspend fun getCategories(): Either<NetworkError, Response<MetadataCategories>> {
        return Either.catch {
            categoriesApi.getCategories()
        }.mapLeft { it.toNetworkError() }
    }
}