package com.example.playground.ui.features.auth.login.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.playground.ui.features.auth.signup.componets.ReuseAbleTextField

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    emailError: String = "",
    passwordError: String = "",
) {

    ReuseAbleTextField(
        value = email,
        onValueChange = onEmailChange,
        label = "Email",
        hint = "username123@gmail.com",
        leadingIcon = Icons.Default.MailOutline,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        maxLines = 1,
    )

    // Show email error
    if (emailError.isNotEmpty()) {
        Text(
            text = emailError,
            color = Color.Red,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    ReuseAbleTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = "Password",
        leadingIcon = Icons.Default.LockOpen,
        isPassword = true,
        isPasswordVisible = isPasswordVisible,
        onPasswordVisibilityToggle = onPasswordVisibilityToggle,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
    )
    // Show password error
    if (passwordError.isNotEmpty()) {
        Text(
            text = passwordError,
            color = Color.Red,
            modifier = Modifier.fillMaxWidth()
        )
    }

}