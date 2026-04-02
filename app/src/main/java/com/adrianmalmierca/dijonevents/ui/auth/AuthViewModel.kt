package com.adrianmalmierca.dijonevents.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrianmalmierca.dijonevents.data.repository.AuthRepository
import com.adrianmalmierca.dijonevents.data.repository.TokenManager
import com.adrianmalmierca.dijonevents.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    tokenManager: TokenManager
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = tokenManager.token
        .map { !it.isNullOrEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> _uiState.value = AuthUiState(success = true, isLoading = false)
                is Result.Error -> _uiState.value =_uiState.value.copy(
                    error = result.message, isLoading = false
                )
                else -> {}
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = authRepository.register(name, email, password)) {
                is Result.Success -> _uiState.value = AuthUiState(success = true, isLoading = false)
                is Result.Error -> _uiState.value =_uiState.value.copy(
                    error = result.message, isLoading = false
                )
                else -> {}
            }
        }
    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
