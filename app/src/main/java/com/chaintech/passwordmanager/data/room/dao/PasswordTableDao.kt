package com.pixsterstudio.composearchitecture.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chaintech.passwordmanager.data.room.model.PasswordTable
import kotlinx.coroutines.flow.Flow


@Dao
interface PasswordTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: PasswordTable)

    @Query("SELECT * FROM PasswordTable")
    fun readAllPassword(): Flow<List<PasswordTable>>

    @Query("DELETE FROM PasswordTable WHERE id = :id")
    suspend fun deletePassword(id: String)
}