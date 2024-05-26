package com.selegend.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.domain.model.MetadataCategories
import com.selegend.ecommercemobile.store.domain.model.Response

interface CategoriesRepository {
    suspend fun getCategories(): Either<NetworkError, Response<MetadataCategories>>
}