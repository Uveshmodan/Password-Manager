package com.pixsterstudio.composearchitecture.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chaintech.passwordmanager.data.room.model.PasswordTable
import com.pixsterstudio.composearchitecture.data.room.dao.PasswordTableDao

@Database(
    entities = [PasswordTable::class], version = 1, exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun PasswordTableDao(): PasswordTableDao
}