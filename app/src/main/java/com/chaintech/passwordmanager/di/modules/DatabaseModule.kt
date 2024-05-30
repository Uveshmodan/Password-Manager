package com.pixsterstudio.composearchitecture.di.modules

import android.content.Context
import androidx.room.Room
import com.pixsterstudio.composearchitecture.data.room.AppDataBase
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDataBase::class.java, "password_manager_db")
        .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providePasswordTableDao(database: AppDataBase) = database.PasswordTableDao()

}