package com.example.frontend.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

open class UserInterfaceViewModel: ViewModel() {
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