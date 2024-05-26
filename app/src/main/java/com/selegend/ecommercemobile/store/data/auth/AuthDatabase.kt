package com.selegend.ecommercemobile.store.data.auth

import androidx.room.Database
import androidx.room.RoomDatabase
import com.selegend.ecommercemobile.store.data.remote.AuthDao
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth

@Database(
    entities = [Auth::class],
    version = 1,
    exportSchema = true
)
abstract class AuthDatabase: RoomDatabase() {
    abstract val dao: AuthDao
}