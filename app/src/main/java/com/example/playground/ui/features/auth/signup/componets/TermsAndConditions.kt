package com.example.dlete.ui.features.auth.signup.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp


@Composable
fun TermsAndConditions(
    isAccepted: Boolean,
    onAcceptedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAcceptedChange(!isAccepted) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isAccepted,
            onCheckedChange = onAcceptedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = AppColors.Primary,
                uncheckedColor = AppColors.Primary
            )
        )
        Text(
            text = "Terms of use and privacy policy",
            fontSize = 14.sp,
            color = AppColors.TextSecondary
        )
    }
}