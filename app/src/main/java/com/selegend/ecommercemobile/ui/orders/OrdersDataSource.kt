package com.selegend.ecommercemobile.ui.orders

import androidx.paging.PagingSource
import androidx.paging.PagingState
import arrow.core.Either
import com.selegend.ecommercemobile.store.domain.model.MetadataOrders
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.orders.OrderHistory
import com.selegend.ecommercemobile.store.domain.repository.CheckoutRepository

class OrdersDataSource (
    private val checkoutRepository: CheckoutRepository,
    private val ordersParams: OrdersParams
) : PagingSource<Int, OrderHistory>() {
    override fun getRefreshKey(state: PagingState<Int, OrderHistory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderHistory> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = checkoutRepository.getAllOrders(
                headers = ordersParams.headers,
                page = nextPageNumber,
                limit = ordersParams.limit,
                sortBy = ordersParams.sortBy,
                order = ordersParams.order,
                status = ordersParams.status,
                name = ordersParams.name
            )
            val orders: List<OrderHistory> = when(response) {
                is Either.Right -> response.value.metadata.orders
                else -> emptyList<OrderHistory>()
            }
            LoadResult.Page(
                data = orders,
                prevKey = null,
                nextKey = if (response.isRight() && orders.isNotEmpty()) ((response as Either.Right<Response<MetadataOrders>>).value.metadata.page!! + 1) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}