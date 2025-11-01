package com.example.playground.ui.features.auth.signup.view

import android.util.Patterns
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
import com.example.playground.ui.features.auth.signup.componets.RegisterTitle
import com.example.playground.ui.features.auth.signup.componets.SignInLink
import com.example.playground.ui.features.auth.signup.componets.TermsAndConditions
import com.example.playground.navigation.Screen
import com.example.playground.ui.features.auth.authShared.AuthState
import com.example.playground.ui.features.auth.signup.viewModel.SignupViewModel


@Composable
fun RegisterScreen2(
    navController: NavController,
    signUpViewModel: SignupViewModel = viewModel()

) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var isTermsAccepted by remember { mutableStateOf(false) }

    // Simple error states - just like AgriScreen
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var termsError by remember { mutableStateOf("") }


    // Collect signup state
    val signupState by signUpViewModel.signupState.collectAsState()

    // Handle signup state changes
    LaunchedEffect(signupState) {
        when (signupState) {
            is AuthState.Success -> {
                // Navigate to login screen
                navController.navigate(Screen.Login.route)
                signUpViewModel.resetSignupState()
            }
            else -> {}
        }
    }


    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun calculatePasswordStrength(password: String): Int {
        var strength = 0
        if (password.length >= 8) strength++
        if (password.any { it.isDigit() }) strength++
        if (password.any { it.isLowerCase() }) strength++
        if (password.any { it.isUpperCase() }) strength++
        return strength
    }

    fun validateForm(): Boolean {
        var isValid = true

        // Clear previous errors
        nameError = ""
        emailError = ""
        passwordError = ""
        confirmPasswordError = ""
        termsError = ""

        // Name validation
        if (fullName.trim().isEmpty()) {
            nameError = "Name is required"
            isValid = false
        } else if (fullName.trim().length < 3) {
            nameError = "Name must be at least 3 characters"
            isValid = false
        }

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
        } else if (password.length < 8) {
            passwordError = "Password must be at least 8 characters"
            isValid = false
        } else if (calculatePasswordStrength(password) < 3) {
            passwordError = "Password is too weak"
            isValid = false
        } else {
            passwordError = ""
        }

        // Confirm password validation
        if (confirmPassword.isEmpty()) {
            confirmPasswordError = "Please confirm your password"
            isValid = false
        } else if (confirmPassword != password) {
            confirmPasswordError = "Passwords don't match"
            isValid = false
        }

        // Terms validation
        if (!isTermsAccepted) {
            termsError = "You must accept terms and conditions"
            isValid = false
        } else {
            termsError = ""
        }

        return isValid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
    ) {
        // Top leaves decoration
//        Image(
//            painter = painterResource(id = R.drawable.twitter),
//            contentDescription = null,
//            modifier = Modifier
//                .align(Alignment.TopStart)
//                .size(120.dp)
//        )

        // Bottom leaves decoration
//        Image(
//            painter = painterResource(id = R.drawable.plant_leaf),
//            contentDescription = null,
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .size(120.dp)
//        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            RegisterTitle()

            Spacer(modifier = Modifier.height(32.dp))

            RegisterForm(
                fullName = fullName,
                onFullNameChange = {
                    fullName = it
                    if (nameError.isNotEmpty()) nameError = "" // Clear error when typing
                },
                email = email,
                onEmailChange = {
                    email = it
                    if (emailError.isNotEmpty()) emailError = ""
                },
                password = password,
                onPasswordChange = {
                    password = it
                    if (passwordError.isNotEmpty()) passwordError = ""
                },
                isPasswordVisible = isPasswordVisible,
                onPasswordVisibilityToggle = { isPasswordVisible = !isPasswordVisible },
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = {
                    confirmPassword = it
                    if (confirmPasswordError.isNotEmpty()) confirmPasswordError = ""
                },
                isConfirmPasswordVisible = isConfirmPasswordVisible,
                onConfirmPasswordVisibilityToggle = {
                    isConfirmPasswordVisible = !isConfirmPasswordVisible
                },
                nameError = nameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )

            Spacer(modifier = Modifier.height(16.dp))

            TermsAndConditions(
                isAccepted = isTermsAccepted,
                onAcceptedChange = { isTermsAccepted = it }
            )

            if (termsError.isNotEmpty()) {
                Text(
                    text = termsError,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Show signup error from ViewModel
            if (signupState is AuthState.Error) {
                Text(
                    text = (signupState as AuthState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ReuseAbleButton(
                text = if (signupState is AuthState.Loading) "Loading..." else "Register",
                onClick = {
                    if (validateForm()) {
                        signUpViewModel.signUp(email, password, fullName)
                    }
                },
                enabled = signupState !is AuthState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            SignInLink(
                onSignInClick = {
                    navController.navigate(Screen.Login.route)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }




}