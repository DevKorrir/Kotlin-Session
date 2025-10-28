package com.example.playground.ui.features.auth.login.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dlete.ui.features.auth.signup.componets.ReuseAbleTextField

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
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
            imeAction = ImeAction.Next
        ),
    )

}