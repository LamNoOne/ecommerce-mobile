package com.example.ecommercemobile.ui.products

import androidx.paging.PagingSource
import androidx.paging.PagingState
import arrow.core.Either
import com.example.ecommercemobile.store.domain.model.MetadataProducts
import com.example.ecommercemobile.store.domain.model.ProductParams
import com.example.ecommercemobile.store.domain.model.Response
import com.example.ecommercemobile.store.domain.model.core.Product
import com.example.ecommercemobile.store.domain.repository.ProductsRepository

class ProductsDataSource(
    private val productsRepository: ProductsRepository,
    private val productParams: ProductParams
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = productsRepository.getProducts(
                name = productParams.name,
                categoryId = productParams.categoryId,
                page = nextPageNumber,
                limit = productParams.limit,
                sortBy = productParams.sortBy,
                order = productParams.order
            )
            val products = when(response) {
                is Either.Right -> (response as Either.Right<Response<MetadataProducts>>).value.metadata.products
                else -> emptyList<Product>()
            }
            LoadResult.Page(
                data = products,
                prevKey = null,
                nextKey = if (response.isRight() && products.isNotEmpty()) ((response as Either.Right<Response<MetadataProducts>>).value.metadata?.page!! + 1) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}