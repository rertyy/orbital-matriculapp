package com.example.frontend.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.CreationExtras.Empty.map
import com.example.frontend.data.prefs.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

// https://blog.canopas.com/exploring-data-store-a-new-way-of-storing-data-in-android-fb97c2ae298
// TODO add refresh token (pending backend)


class TokenStore(private val context: Context) {

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
    suspend fun storeJwtToken(accessToken: String) {
        datastore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
        Log.d("GET THREAD", "STORED IN DATASTORE $accessToken")
    }


    suspend fun getTokenFlow(): String {
        val token = datastore.data.first()
        return token[ACCESS_TOKEN] ?: "token absent"

    }
}
