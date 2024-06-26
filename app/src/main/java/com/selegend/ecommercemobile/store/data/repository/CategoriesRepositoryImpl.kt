package com.selegend.ecommercemobile.store.data.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.data.mapper.toNetworkError
import com.selegend.ecommercemobile.store.domain.remote.CategoriesApi
import com.selegend.ecommercemobile.store.domain.model.MetadataCategories
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.repository.CategoriesRepository
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