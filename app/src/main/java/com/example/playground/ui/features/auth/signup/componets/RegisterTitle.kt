package com.example.dlete.ui.features.auth.signup.componets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun RegisterTitle() {
    Text(
        text = "Register",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = AppColors.Primary
    )
}