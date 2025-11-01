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
        if (password.isEmpty() || password.length < 8) {
            _loginState.value = AuthState.Error("Password must be at least 8 characters")
            return
        }

        _loginState.value = AuthState.Loading
        
        viewModelScope.launch {
            try {
                val result: AuthResult = auth.signInWithEmailAndPassword(email, password).await()
                //_loginState.value = AuthState.Success("Login successful!")
                // Check if email is verified
                val user = result.user
                if (user != null) {
                    if (user.isEmailVerified) {
                        _loginState.value = AuthState.Success("Login successful!")
                    } else {
                        // If email not verified, send verification again and show message
                        user.sendEmailVerification().await()
                        _loginState.value = AuthState.Error(
                            "Please verify your email first. We've sent a new verification email to ${user.email}"
                        )
                        // Sign out the user since email is not verified
                        auth.signOut()
                    }
                }
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