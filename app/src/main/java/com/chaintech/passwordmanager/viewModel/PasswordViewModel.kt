package com.chaintech.passwordmanager.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaintech.passwordmanager.data.room.model.PasswordTable
import com.chaintech.passwordmanager.utils.EncryptionUtil
import com.pixsterstudio.composearchitecture.data.room.repository.PasswordTableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val passwordTableRepository: PasswordTableRepository
): ViewModel() {

    fun readAllPassword() = passwordTableRepository.readAllPassword()

    fun insertPassword(password: PasswordTable) = viewModelScope.launch {
        val encryptedString = EncryptionUtil.encrypt(password.password)
        val encrypted = password.copy(password = encryptedString)
        passwordTableRepository.insertPassword(password = encrypted)
    }

    fun deletePassword(id: String) = viewModelScope.launch {
        passwordTableRepository.deletePassword(id = id)
    }

    fun editPassword(password: PasswordTable) = viewModelScope.launch {
        val encryptedString = EncryptionUtil.encrypt(password.password)
        val encrypted = password.copy(password = encryptedString)
        passwordTableRepository.insertPassword(encrypted)
    }
}
