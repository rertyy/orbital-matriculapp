package com.example.frontend.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

// https://blog.canopas.com/exploring-data-store-a-new-way-of-storing-data-in-android-fb97c2ae298
// TODO add refresh token (pending backend)


class TokenStore(context: Context) {

    // Create the dataStore and give it a name same as user_pref
    // Create some keys we will use them to store and retrieve the data
    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_token")

    }


    private val datastore = context.dataStore

    // Store user data
    // refer to the data store and using edit
    // we can store values using the keys
    suspend fun storeToken(accessToken: String) {
        datastore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
        Log.d("JWT TOKEN", "STORED IN DATASTORE $accessToken")
    }


    suspend fun getToken(): String {
        val token = datastore.data.first()
        return token[ACCESS_TOKEN] ?: "tokenNotFound".also { Log.d("JWT TOKEN", "TOKEN NOT FOUND") }

    }
}
