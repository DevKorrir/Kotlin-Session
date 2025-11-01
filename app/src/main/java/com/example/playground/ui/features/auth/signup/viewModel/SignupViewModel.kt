package com.example.playground.ui.features.auth.signup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playground.ui.features.auth.authShared.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignupViewModel : ViewModel() {
    
    private val auth = FirebaseAuth.getInstance()
    
    // UI State
    private val _signupState = MutableStateFlow<AuthState>(AuthState.Idle)
    val signupState: StateFlow<AuthState> = _signupState

    // Signup with Firebase
    fun signUp(email: String, password: String, fullName: String) {
        // Basic validation
        if (fullName.isEmpty() || fullName.length < 3) {
            _signupState.value = AuthState.Error("Name must be at least 3 characters")
            return
        }
        if (email.isEmpty() || !isValidEmail(email)) {
            _signupState.value = AuthState.Error("Please enter a valid email")
            return
        }
        if (password.isEmpty() || password.length < 8) {
            _signupState.value = AuthState.Error("Password must be at least 8 characters")
            return
        }

        _signupState.value = AuthState.Loading
        
        viewModelScope.launch {
            try {
                val result: AuthResult = auth.createUserWithEmailAndPassword(email, password).await()
                
                // Update user profile with display name
                result.user?.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()
                )?.await()

                // Send email verification
                result.user?.sendEmailVerification()?.await()

                _signupState.value = AuthState.Success(
                    "Account created! Please check your email to verify your account before logging in."
                )
            } catch (e: Exception) {
                _signupState.value = AuthState.Error("Signup failed: ${e.message}")
            }
        }
    }

    // Reset state
    fun resetSignupState() {
        _signupState.value = AuthState.Idle
    }

    // Simple email validation
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}