package com.example.playground.ui.features.auth.login.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.playground.ui.features.auth.signup.componets.AppColors
import com.example.playground.ui.features.auth.signup.componets.ReuseAbleButton
import com.example.playground.navigation.Screen
import com.example.playground.ui.features.auth.login.componets.LogInLink
import com.example.playground.ui.features.auth.login.componets.LoginTitle
import com.example.playground.ui.features.auth.login.componets.RememberAndForgot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    navController: NavController
) {

    var email by remember { mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var isPasswordVisible by remember { mutableStateOf(false)}
    var isRememberMe by remember { mutableStateOf(false)}

    // Error states
    var emailError by remember { mutableStateOf("")}
    var passwordError by remember { mutableStateOf("")}

    // Simple validation function
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateLoginForm(): Boolean {
        var isValid = true

        // Clear previous errors
        emailError = ""
        passwordError = ""

        // Email validation
        if (email.trim().isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!isValidEmail(email)) {
            emailError = "Enter a valid email address"
            isValid = false
        }

        // Password validation
        if (password.isEmpty()) {
            passwordError = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        }

        return isValid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            LoginTitle()

            Spacer(modifier = Modifier.height(32.dp))

            LoginForm(
                modifier = Modifier,
                email = email ,
                onEmailChange = {
                    email = it
                    if (emailError.isNotEmpty()) emailError = "" // Clear error when typing
                },
                password = password,
                onPasswordChange = {
                    password = it
                    if (passwordError.isNotEmpty()) passwordError = ""
                },
                emailError = emailError,
                passwordError = passwordError,
                onDone = {
                    if (validateLoginForm()) {
                        onLoginSuccess()
                    }
                },
                isPasswordVisible = isPasswordVisible,
                onPasswordVisibilityToggle =  {
                    isPasswordVisible = !isPasswordVisible
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RememberAndForgot(
                isRememberMe = isRememberMe,
                onRememberChange = { isRememberMe = it },
                onForgotClick = {

                }
            )


            Spacer(modifier = Modifier.height(24.dp))

            ReuseAbleButton(
                text = "Login",
                onClick = {
                    if (validateLoginForm()) {
                        onLoginSuccess()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LogInLink(
                onLogInClick = {
                    navController.navigate(Screen.Signup.route)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

    }


}