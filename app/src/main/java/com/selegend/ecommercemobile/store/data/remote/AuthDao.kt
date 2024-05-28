package com.selegend.ecommercemobile.store.data.remote

import androidx.room.*
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth

/**
 * DAO interface for the Auth table.
 * This class represents the Data Access Object (DAO) for the Auth table.
 * It provides methods to perform database operations on the Auth table.
 */
@Dao
interface AuthDao {

    /**
     * Inserts a new Auth object into the Auth table.
     * If the Auth object already exists, it replaces it.
     * @param auth The Auth object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuth(auth: Auth)

    /**
     * Deletes a specific Auth object from the Auth table.
     * @param auth The Auth object to be deleted.
     */
    @Delete
    suspend fun deleteAuth(auth: Auth)

    /**
     * Retrieves a specific Auth object from the Auth table by its id.
     * @param id The id of the Auth object to be retrieved.
     * @return The Auth object with the specified id, or null if no such object exists.
     */
    @Query("SELECT * FROM auth WHERE id = :id")
    suspend fun getAuthById(id: Int): Auth?

}