package com.example.frontend.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

open class UserInterfaceViewModel : ViewModel() {
    var username: String by mutableStateOf("")
        private set
    var password: String by mutableStateOf("")
        private set

    fun changeUsername(username: String) {
        this.username = username
    }

    fun changePassword(password: String) {
        this.password = password
    }
}