package com.chaintech.passwordmanager.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class PasswordTable(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var accountName: String = "",
    var emailUname: String = "",
    var password: String = ""
)
