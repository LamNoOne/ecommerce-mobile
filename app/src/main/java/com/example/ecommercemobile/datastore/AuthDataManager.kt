package com.example.ecommercemobile.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class AuthDataManager(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "AUTH_DATASTORE")

    companion object {
        val USER_ID = intPreferencesKey("USER_ID")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }

    suspend fun saveAuth(settings: AuthPreferences) {
        context.dataStore.edit {
            it[USER_ID] = settings.userId
            it[ACCESS_TOKEN] = settings.accessToken
            it[REFRESH_TOKEN] = settings.refreshToken
        }
    }

    suspend fun getAuth() = context.dataStore.data.map {
        AuthPreferences(
            userId = it[USER_ID] ?: 0,
            accessToken = it[ACCESS_TOKEN] ?: "",
            refreshToken = it[REFRESH_TOKEN] ?: "",
        )
    }
}