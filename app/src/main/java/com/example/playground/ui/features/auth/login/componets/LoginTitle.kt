package com.example.playground.ui.features.auth.login.componets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.dlete.ui.features.auth.signup.componets.AppColors

@Composable
fun LoginTitle(
    modifier: Modifier = Modifier
) {

    Text(
        text = "Register",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = AppColors.Primary
    )

}