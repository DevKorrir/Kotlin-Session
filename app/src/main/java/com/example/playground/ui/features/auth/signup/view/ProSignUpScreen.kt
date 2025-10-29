package com.example.dlete.ui.features.auth.signup.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.playground.ui.features.auth.signup.componets.RegisterTitle
import com.example.playground.ui.features.auth.signup.componets.SignInLink
import com.example.playground.ui.features.auth.signup.componets.TermsAndConditions
import com.example.playground.navigation.Screen


@Composable
fun RegisterScreen2(
    navController: NavController
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var isTermsAccepted by remember { mutableStateOf(false) }

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
                onFullNameChange = { fullName = it },
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                isPasswordVisible = isPasswordVisible,
                onPasswordVisibilityToggle = { isPasswordVisible = !isPasswordVisible },
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = { confirmPassword = it },
                isConfirmPasswordVisible = isConfirmPasswordVisible,
                onConfirmPasswordVisibilityToggle = { isConfirmPasswordVisible = !isConfirmPasswordVisible }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TermsAndConditions(
                isAccepted = isTermsAccepted,
                onAcceptedChange = { isTermsAccepted = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ReuseAbleButton(
                text = "Sign up",
                onClick = { /* TODO: Handle sign up */ }
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