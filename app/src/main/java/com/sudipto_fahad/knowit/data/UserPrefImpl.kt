package com.sudipto_fahad.knowit.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sudipto_fahad.knowit.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPrefImpl (private val context: Context) : UserPref {

    companion object {
        private val PHONE_NO = stringPreferencesKey("PHONE_NO")
        private val LOCATION = stringPreferencesKey("LOCATION")
        private const val DATASTORE_NAME = "USER"
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)
    }

    override suspend fun saveUserModel(userModel: UserModel) {
        context.datastore.edit { users ->
            users[PHONE_NO] = userModel.phoneNo
            users[LOCATION] = userModel.location
        }
    }

    override fun getUserModel(): Flow<UserModel> = context.datastore.data.map { user ->
        UserModel(
            phoneNo = user[PHONE_NO] ?: "",
            location = user[LOCATION] ?: ""
        )
    }
}