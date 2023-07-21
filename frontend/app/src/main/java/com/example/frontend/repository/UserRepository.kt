package com.example.frontend.repository

import com.example.frontend.ui.screens.auth.LoginRequest
import com.example.frontend.ui.screens.auth.LoginResponse
import com.example.frontend.ui.screens.auth.RegisterRequest
import retrofit2.Response

interface UserRepository {
    suspend fun authenticateLogin(request: LoginRequest): Response<LoginResponse>
    suspend fun registerUser(request: RegisterRequest): Response<RegisterRequest>

    suspend fun storeToken(token: String)
    suspend fun getToken(): String

//    suspend fun logout(): String
//    suspend fun getUser(): String
//    suspend fun editUser(username: String, password: String): String
//    suspend fun deleteUser(): String
}