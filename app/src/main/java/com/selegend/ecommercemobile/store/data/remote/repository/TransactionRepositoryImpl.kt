package com.selegend.ecommercemobile.store.data.remote.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.data.mapper.toNetworkError
import com.selegend.ecommercemobile.store.data.remote.TransactionApi
import com.selegend.ecommercemobile.store.domain.model.MetadataMakeTransaction
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.transaction.MakeTransaction
import com.selegend.ecommercemobile.store.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionApi: TransactionApi
): TransactionRepository {
    override suspend fun createTransaction(
        headers: Map<String, String>,
        makeTransaction: MakeTransaction
    ): Either<NetworkError, Response<MetadataMakeTransaction>> {
        return Either.catch {
            transactionApi.createTransaction(headers, makeTransaction)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getTransaction(
        headers: Map<String, String>,
        transactionId: String
    ): Either<NetworkError, Response<MetadataMakeTransaction>> {
        return Either.catch {
            transactionApi.getTransaction(headers, transactionId)
        }.mapLeft {
            it.toNetworkError()
        }
    }
}