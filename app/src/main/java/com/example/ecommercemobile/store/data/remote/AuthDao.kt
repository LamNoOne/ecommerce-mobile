package com.example.ecommercemobile.store.data.remote

import androidx.room.*
import com.example.ecommercemobile.store.domain.model.core.Auth

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuth(auth: Auth)

    @Delete
    suspend fun deleteAuth(auth: Auth)

    @Query("SELECT * FROM auth WHERE id = :id")
    suspend fun getAuthById(id: Int): Auth?
}