package com.pixsterstudio.composearchitecture.data.room.repository

import com.chaintech.passwordmanager.data.room.model.PasswordTable
import com.pixsterstudio.composearchitecture.data.room.dao.PasswordTableDao
import javax.inject.Inject

class PasswordTableRepository @Inject constructor(
  private val passwordTableDao: PasswordTableDao
) {
    fun readAllPassword() = passwordTableDao.readAllPassword()

    suspend fun insertPassword(password: PasswordTable) = passwordTableDao.insertPassword(password = password)

    suspend fun deletePassword(id: String) = passwordTableDao.deletePassword(id = id)
}