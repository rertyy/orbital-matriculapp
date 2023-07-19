package com.example.frontend.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// https://blog.canopas.com/exploring-data-store-a-new-way-of-storing-data-in-android-fb97c2ae298
// TODO add refresh token (pending backend)


class SaveTokenInfo(private val context: Context) {

    // Create the dataStore and give it a name same as user_pref
    // Create some keys we will use them to store and retrieve the data
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_token")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
    }

    // Store user data
    // refer to the data store and using edit
    // we can store values using the keys
    suspend fun storeJwtToken(accessToken: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    // get access token
    val userNameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN] ?: ""
    }
}
