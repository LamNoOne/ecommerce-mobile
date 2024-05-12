package com.example.ecommercemobile.di

import com.example.ecommercemobile.core.AnnotationInterceptor
import com.example.ecommercemobile.store.data.remote.CategoriesApi
import com.example.ecommercemobile.store.data.remote.ProductsApi
import com.example.ecommercemobile.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    private val annotationInterceptor = AnnotationInterceptor()
    private val client = OkHttpClient().newBuilder().apply {
        addInterceptor(annotationInterceptor)
    }.build()

    @Provides
    @Singleton
    fun provideProductApi(): ProductsApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .client(client)
            .build()
            .create(ProductsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(): CategoriesApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .client(client)
            .build()
            .create(CategoriesApi::class.java)
    }
}