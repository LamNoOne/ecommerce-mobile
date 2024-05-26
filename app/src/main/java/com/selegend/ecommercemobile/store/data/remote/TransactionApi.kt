package com.selegend.ecommercemobile.store.data.remote

import com.selegend.ecommercemobile.store.domain.model.MetadataMakeTransaction
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.transaction.MakeTransaction
import retrofit2.http.*

interface TransactionApi {

    @POST("transaction")
    suspend fun createTransaction(
        @HeaderMap headers: Map<String, String>,
        @Body makeTransaction: MakeTransaction
    ): Response<MetadataMakeTransaction>

    @GET("transaction")
    suspend fun getTransaction(
        @HeaderMap headers: Map<String, String>,
        @Query("transactionId") transactionId: String
    ): Response<MetadataMakeTransaction>
}