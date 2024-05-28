package com.selegend.ecommercemobile.ui.products

const val SORT_DEFAULT = "price"

const val SEARCH_DEFAULT = ""

const val LIMIT_DEFAULT = 20

const val CATEGORY_ID_DEFAULT = ""

enum class ORDER(val value: String) {
    ASC("asc"),
    DESC("desc"),
    DEFAULT("")
}

sealed class ProductsEvent {

    data class OnProductClick(val productId: Int) : ProductsEvent()

    data class OnWishListProductClick(val productId: Int) : ProductsEvent()

    data class OnBestMatchSelected(
        val search :String ?= SEARCH_DEFAULT,
        val categoryId: String ?= CATEGORY_ID_DEFAULT,
        val limit: Int = LIMIT_DEFAULT,
        val sortBy :String = SORT_DEFAULT,
        val order :String = ORDER.DEFAULT.value,
    ) : ProductsEvent()

    data class OnPriceLowToHighSelected(
        val search :String ?= SEARCH_DEFAULT,
        val categoryId: String ?= CATEGORY_ID_DEFAULT,
        val limit: Int = LIMIT_DEFAULT,
        val sortBy :String = SORT_DEFAULT,
        val order :String = ORDER.ASC.value,
    ) : ProductsEvent()

    data class OnPriceHighToLowSelected(
        val search :String ?= SEARCH_DEFAULT,
        val categoryId: String ?= CATEGORY_ID_DEFAULT,
        val limit: Int = LIMIT_DEFAULT,
        val sortBy :String = SORT_DEFAULT,
        val order :String = ORDER.DESC.value,
    ) : ProductsEvent()
}