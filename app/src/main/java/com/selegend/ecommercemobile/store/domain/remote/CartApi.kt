package com.selegend.ecommercemobile.store.domain.remote

import com.selegend.ecommercemobile.store.domain.model.MetadataCart
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.carts.AddCart
import com.selegend.ecommercemobile.store.domain.model.core.carts.DeleteCart
import com.selegend.ecommercemobile.store.domain.model.core.carts.GetSelectedProduct
import com.selegend.ecommercemobile.store.domain.model.core.carts.UpdateCart
import retrofit2.http.*

/**
 * This interface represents the API for the Cart functionality in the application.
 * It includes methods for getting the cart, adding to the cart, updating product quantity in the cart,
 * and deleting products from the cart.
 */
interface CartApi {
    /**
     * Get the current user's cart.
     * @param headers The headers to be sent with the request.
     * @return Response containing MetadataCart
     */
    @GET("carts/get-my-cart")
    suspend fun getCart(
        @HeaderMap headers: Map<String, String>,
    ): Response<MetadataCart>

    @POST("carts/get-selected-products")
    suspend fun getSelectedProducts(
        @HeaderMap headers: Map<String, String>,
        @Body selectedProducts: GetSelectedProduct
    ): Response<MetadataCart>

    /**
     * Add a product to the current user's cart.
     * @param headers The headers to be sent with the request.
     * @param addCart The product to be added to the cart.
     * @return Response containing MetadataCart
     */
    @POST("carts/add-to-cart")
    suspend fun addToCart(
        @HeaderMap headers: Map<String, String>,
        @Body addCart: AddCart
    ): Response<MetadataCart>

    /**
     * Update the quantity of a product in the current user's cart.
     * @param headers The headers to be sent with the request.
     * @param addCart The product whose quantity is to be updated.
     * @return Response containing MetadataCart
     */
    @PATCH("carts/update-quantity-product")
    suspend fun updateQuantityProduct(
        @HeaderMap headers: Map<String, String>,
        @Body updateCart: UpdateCart
    ): Response<MetadataCart>

    /**
     * Delete a product from the current user's cart.
     * @param headers The headers to be sent with the request.
     * @param productId The id of the product to be deleted.
     * @return Response containing MetadataCart
     */
    @DELETE("carts/delete-product-from-cart")
    suspend fun deleteProductFromCart(
        @HeaderMap headers: Map<String, String>,
        @Query("productId") productId: Int
    ): Response<MetadataCart>

    /**
     * Delete multiple products from the current user's cart.
     * @param headers The headers to be sent with the request.
     * @param deleteCart The products to be deleted from the cart.
     * @return Response containing MetadataCart
     */
    @DELETE("carts/delete-products-from-cart")
    suspend fun deleteProductsFromCart(
        @HeaderMap headers: Map<String, String>,
        @Body deleteCart: DeleteCart
    ): Response<MetadataCart>
}