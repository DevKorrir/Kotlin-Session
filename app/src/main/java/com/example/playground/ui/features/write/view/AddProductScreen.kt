package com.example.playground.ui.features.write.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.playground.ui.features.auth.signup.componets.ReuseAbleButton
import com.example.playground.ui.features.auth.signup.componets.ReuseAbleTextField
import com.example.playground.ui.features.write.viewmodel.ProductUiState
import com.example.playground.ui.features.write.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val productUiState by viewModel.productUiState.collectAsState()

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(productUiState) {
        when (val state = productUiState) {
            is ProductUiState.Success -> {
                Toast.makeText(context, "Product saved successfully", Toast.LENGTH_SHORT).show()
                name = ""
                description = ""
                price = ""
                category = ""
                imageUri = null
                viewModel.resetState() // Reset state in ViewModel
            }
            is ProductUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Image picker
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { launcher.launch("image/*") }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Product image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.AddAPhoto,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Text("Tap to add image")
                    }
                }
            }
        }

        // Input fields
        ReuseAbleTextField(
            value = name,
            onValueChange = { name = it },
            label = "Product Name *",
            leadingIcon = Icons.Default.ShoppingBag,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        ReuseAbleTextField(
            value = description,
            onValueChange = { description = it },
            label = "Description",
            leadingIcon = Icons.Default.Description,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        ReuseAbleTextField(
            value = price,
            onValueChange = { price = it },
            label = "Price *",
            leadingIcon = Icons.Default.AttachMoney,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        ReuseAbleTextField(
            value = category,
            onValueChange = { category = it },
            label = "Category",
            leadingIcon = Icons.Default.Category,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )

        // Save button
        ReuseAbleButton(
            text = "Save Product",
            onClick = {
                if (name.isNotBlank() && price.isNotBlank()) {
                    val priceValue = price.toDoubleOrNull() ?: 0.0
                    viewModel.addProduct(
                        name = name,
                        description = description,
                        price = priceValue,
                        imageUri = imageUri?.toString(),
                        category = category
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            loading = productUiState is ProductUiState.Loading,
            enabled = name.isNotBlank() && price.isNotBlank()
        )
    }
}
