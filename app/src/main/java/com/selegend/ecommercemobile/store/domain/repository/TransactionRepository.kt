package com.selegend.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.domain.model.MetadataMakeTransaction
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.transaction.MakeTransaction

interface TransactionRepository {

    suspend fun createTransaction(headers: Map<String, String>, makeTransaction: MakeTransaction): Either<NetworkError, Response<MetadataMakeTransaction>>

    suspend fun getTransaction(headers: Map<String, String>, transactionId: String): Either<NetworkError, Response<MetadataMakeTransaction>>
}