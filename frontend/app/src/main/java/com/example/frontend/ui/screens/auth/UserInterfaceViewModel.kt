package com.example.frontend.ui.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class UserInterfaceViewModel @Inject constructor() : ViewModel() {
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