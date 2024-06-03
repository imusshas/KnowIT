package com.sudipto_fahad.knowit.data

import com.sudipto_fahad.knowit.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserPref {
    suspend fun saveUserModel(userModel: UserModel)

    fun getUserModel(): Flow<UserModel>
}