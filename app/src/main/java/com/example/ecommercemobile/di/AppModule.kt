package com.example.ecommercemobile.di

import android.app.Application
import androidx.room.Room
import com.example.ecommercemobile.core.AnnotationInterceptor
import com.example.ecommercemobile.store.data.auth.AuthDatabase
import com.example.ecommercemobile.store.data.remote.*
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

    private val client = OkHttpClient()
        .newBuilder()
        .apply {
            addInterceptor(annotationInterceptor)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideProductApi(): ProductsApi {
        return retrofit.create(ProductsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(): CategoriesApi {
        return retrofit.create(CategoriesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCartApi(): CartApi {
        return retrofit.create(CartApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCheckoutApi(): CheckoutApi {
        return retrofit.create(CheckoutApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthDatabase(app: Application): AuthDatabase {
        return Room.databaseBuilder(
            app,
            AuthDatabase::class.java,
            "auth_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAuthDao(db: AuthDatabase): AuthDao {
        return db.dao
    }


//    @Provides
//    @Singleton
//    fun provideAuthRepository(db: AuthDatabase, authApi: AuthApi): AuthRepository {
//        return AuthRepositoryImpl(db.dao, authApi)
//    }
}