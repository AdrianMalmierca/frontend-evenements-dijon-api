package com.adrianmalmierca.dijonevents.data.repository

import com.adrianmalmierca.dijonevents.data.api.DijonEventsApi
import com.adrianmalmierca.dijonevents.data.model.LoginRequest
import com.adrianmalmierca.dijonevents.data.model.RegisterRequest
import com.adrianmalmierca.dijonevents.util.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: DijonEventsApi,
    private val tokenManager: TokenManager
) {
    suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            val response = api.register(RegisterRequest(name, email, password))
            tokenManager.saveAuth(response.token, response.name, response.email)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Register error")
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val response = api.login(LoginRequest(email, password))
            tokenManager.saveAuth(response.token, response.name, response.email)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Log in error")
        }
    }

    suspend fun logout() {
        tokenManager.clearAuth()
    }
}
