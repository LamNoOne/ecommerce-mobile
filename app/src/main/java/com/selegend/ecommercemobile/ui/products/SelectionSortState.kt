package com.selegend.ecommercemobile.ui.products

data class SelectionSortState(
    var isBestMatch: Boolean = true,
    val isPriceLowToHigh: Boolean = false,
    val isPriceHighToLow: Boolean = false,
)
