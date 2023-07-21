package com.example.frontend.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

val dataStore = context.createDataStore(name = "my_datastore")

data class UserSettings(val username: String, val isDarkModeEnabled: Boolean)

class MyViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = application.createDataStore(name = "my_datastore")

    fun getUserSettings(): LiveData<UserSettings> {
        return dataStore.data
            .map { preferences ->
                UserSettings(
                    preferences[USERNAME_KEY] ?: "",
                    preferences[IS_DARK_MODE_ENABLED_KEY] ?: false
                )
            }
            .asLiveData()
    }

    fun setUsername(username: String) {
        viewModelScope.launch {
            writeUsername(username)
        }
    }

    fun setDarkModeEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            writeDarkModeEnabled(isEnabled)
        }
    }
}