package com.example.playground.ui.features.auth.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playground.ui.features.auth.authShared.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    
    private val auth = FirebaseAuth.getInstance()
    
    // UI State
    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    // Login with Firebase
    fun login(email: String, password: String) {
        // Basic validation
        if (email.isEmpty() || !isValidEmail(email)) {
            _loginState.value = AuthState.Error("Please enter a valid email")
            return
        }
        if (password.isEmpty() || password.length < 6) {
            _loginState.value = AuthState.Error("Password must be at least 6 characters")
            return
        }

        _loginState.value = AuthState.Loading
        
        viewModelScope.launch {
            try {
                val result: AuthResult = auth.signInWithEmailAndPassword(email, password).await()
                _loginState.value = AuthState.Success("Login successful!")
            } catch (e: Exception) {
                _loginState.value = AuthState.Error("Login failed: ${e.message}")
            }
        }
    }

    // Reset state
    fun resetLoginState() {
        _loginState.value = AuthState.Idle
    }

    // Simple email validation
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}