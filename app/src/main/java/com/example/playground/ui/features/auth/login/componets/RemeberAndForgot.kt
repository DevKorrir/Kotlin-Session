package com.example.playground.ui.features.auth.login.componets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.playground.ui.features.auth.signup.componets.AppColors

@Composable
fun RememberAndForgot (
    isRememberMe: Boolean,
    onRememberChange : (Boolean) -> Unit,
    onForgotClick : () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
            //.clickable { onRememberChange(!isRememberMe) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isRememberMe,
            onCheckedChange = {
                onRememberChange(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = AppColors.Primary,
                uncheckedColor = AppColors.Primary
            )
        )
        Text(
            text = "Remember Me",
            fontSize = 14.sp,
            color = AppColors.TextSecondary
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Forgot Password",
            fontSize = 14.sp,
            color = AppColors.TextSecondary
        )
    }
}