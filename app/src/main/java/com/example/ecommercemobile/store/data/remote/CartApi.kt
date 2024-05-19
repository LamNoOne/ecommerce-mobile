package com.example.ecommercemobile.store.data.remote

import com.example.ecommercemobile.store.domain.model.MetadataCart
import com.example.ecommercemobile.store.domain.model.Response
import com.example.ecommercemobile.store.domain.model.core.carts.AddCart
import com.example.ecommercemobile.store.domain.model.core.carts.DeleteCart
import retrofit2.http.*

/**
 * This interface represents the API for the Cart functionality in the application.
 * It includes methods for getting the cart, adding to the cart, updating product quantity in the cart,
 * and deleting products from the cart.
 */
interface CartApi {
    /**
     * Get the current user's cart.
     * @return Response containing MetadataCart
     */

    @GET("carts/get-my-cart")
    suspend fun getCart(
        @HeaderMap headers: Map<String, String>
    ): Response<MetadataCart>

    /**
     * Add a product to the current user's cart.
     * @param addCart The product to be added to the cart.
     * @return Response containing MetadataCart
     */

    @POST("carts/add-to-cart")
    suspend fun addToCart(@Body addCart: AddCart): Response<MetadataCart>

    /**
     * Update the quantity of a product in the current user's cart.
     * @param addCart The product whose quantity is to be updated.
     * @return Response containing MetadataCart
     */

    @POST("carts/update-quantity-product")
    suspend fun updateQuantityProduct(@Body addCart: AddCart): Response<MetadataCart>

    /**
     * Delete a product from the current user's cart.
     * @param productId The id of the product to be deleted.
     * @return Response containing MetadataCart
     */

    @DELETE("carts/delete-product-from-cart")
    suspend fun deleteProductFromCart(@Query("productId") productId: Int): Response<MetadataCart>

    /**
     * Delete multiple products from the current user's cart.
     * @param deleteCart The products to be deleted from the cart.
     * @return Response containing MetadataCart
     */

    @DELETE("carts/delete-products-from-cart")
    suspend fun deleteProductsFromCart(@Body deleteCart: DeleteCart): Response<MetadataCart>
}