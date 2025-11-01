package com.example.playground.ui.features.auth.login.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.playground.ui.features.auth.signup.componets.AppColors
import com.example.playground.ui.features.auth.signup.componets.ReuseAbleButton
import com.example.playground.navigation.Screen
import com.example.playground.ui.features.auth.authShared.AuthState
import com.example.playground.ui.features.auth.login.componets.LogInLink
import com.example.playground.ui.features.auth.login.componets.LoginTitle
import com.example.playground.ui.features.auth.login.componets.RememberAndForgot
import com.example.playground.ui.features.auth.login.viewModel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {

    var email by remember { mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var isPasswordVisible by remember { mutableStateOf(false)}
    var isRememberMe by remember { mutableStateOf(false)}

    // Error states
    var emailError by remember { mutableStateOf("")}
    var passwordError by remember { mutableStateOf("")}

    //collect login state
    val loginState by loginViewModel.loginState.collectAsState()

    // Handle login state changes
    LaunchedEffect(loginState) {
        when (loginState) {
            is AuthState.Success -> {
                // Navigate to home screen
                onLoginSuccess()

                loginViewModel.resetLoginState()
            }
            else -> {}
        }
    }


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

            if (loginState is AuthState.Error) {
                Text(
                    text = (loginState as AuthState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            RememberAndForgot(
                isRememberMe = isRememberMe,
                onRememberChange = { isRememberMe = it },
                onForgotClick = {

                }
            )


            Spacer(modifier = Modifier.height(24.dp))

            ReuseAbleButton(
                text = if (loginState is AuthState.Loading) "Loading..." else "Login",
                onClick = {
                    if (validateLoginForm()) {
                        loginViewModel.login(email, password)
                    }
                },
                enabled = loginState !is AuthState.Loading
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