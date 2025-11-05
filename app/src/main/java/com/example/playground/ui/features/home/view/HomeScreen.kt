package com.example.playground.ui.features.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playground.ui.features.settings.view.ThemeToggle
import com.example.playground.ui.features.sharedPreference.themePrefs.viewModel.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    //onOpenDrawer: () -> Unit
) {

        Column (
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Welcome to Home!",
                modifier = Modifier
            )

            val viewModel: ThemeViewModel = viewModel() // will profvide appro factory fot android viewmodel

            ThemeToggle(
                viewModel = viewModel
            )
        }

}
