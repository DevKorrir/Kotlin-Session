package com.example.playground.ui.features.auth.login.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playground.ui.features.auth.signup.componets.AppColors

@Composable
fun LoginTitle(
    modifier: Modifier = Modifier
) {

    Column {
        Text(
            text = "Welcome Back",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.Primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Login to your account",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraLight,
            color = AppColors.Primary
        )
    }

}