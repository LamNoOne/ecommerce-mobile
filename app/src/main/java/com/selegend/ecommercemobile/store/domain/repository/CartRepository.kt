package com.selegend.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.domain.model.MetadataCart
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.carts.AddCart
import com.selegend.ecommercemobile.store.domain.model.core.carts.DeleteCart
import com.selegend.ecommercemobile.store.domain.model.core.carts.GetSelectedProduct
import com.selegend.ecommercemobile.store.domain.model.core.carts.UpdateCart

interface CartRepository {

    suspend fun getCart(
        headers: Map<String, String>
    ): Either<NetworkError, Response<MetadataCart>>

    suspend fun getSelectedProducts(
        headers: Map<String, String>,
        selectedProducts: GetSelectedProduct
    ): Either<NetworkError, Response<MetadataCart>>

    suspend fun addToCart(
        headers: Map<String, String>,
        addCart: AddCart
    ): Either<NetworkError, Response<MetadataCart>>

    suspend fun updateQuantityProduct(
        headers: Map<String, String>,
        updateCart: UpdateCart
    ): Either<NetworkError, Response<MetadataCart>>

    suspend fun deleteProductFromCart(
        headers: Map<String, String>,
        productId: Int
    ): Either<NetworkError, Response<MetadataCart>>

    suspend fun deleteProductsFromCart(
        headers: Map<String, String>,
        deleteCart: DeleteCart
    ): Either<NetworkError, Response<MetadataCart>>

}