package com.example.ecommercemobile.store.data.auth

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommercemobile.store.data.remote.AuthDao
import com.example.ecommercemobile.store.domain.model.core.Auth

@Database(
    entities = [Auth::class],
    version = 1,
    exportSchema = true
)
abstract class AuthDatabase: RoomDatabase() {
    abstract val dao: AuthDao
}