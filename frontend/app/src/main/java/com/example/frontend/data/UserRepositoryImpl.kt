package com.example.frontend.data

import com.example.frontend.network.ApiService
import com.example.frontend.repository.UserRepository
import com.example.frontend.ui.screens.auth.LoginRequest
import com.example.frontend.ui.screens.auth.LoginResponse
import com.example.frontend.ui.screens.auth.RegisterRequest
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val tokenStore: TokenStore
) : UserRepository {
    override suspend fun authenticateLogin(request: LoginRequest): Response<LoginResponse> {
        // TODO modify LoginResponse to include jwt header
        return api.authenticateLogin(request)
    }

    override suspend fun registerUser(request: RegisterRequest): Response<RegisterRequest> {
        return api.registerUser(request)
    }

    override suspend fun storeToken(token: String) {
        tokenStore.storeToken(token)
    }

    override suspend fun getToken(): String {
        return tokenStore.getToken()
    }


}