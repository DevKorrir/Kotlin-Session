package com.example.playground.ui.features.write.elements

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.playground.ui.features.write.data.Product
import com.example.playground.ui.features.write.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    productId: Int = 0,
    onNavigateBack: () -> Unit = {}
) {
    var product by remember { mutableStateOf<Product?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageUri = it
        }
    }
    
    // Load product data
    LaunchedEffect(productId) {
        product = viewModel.getProductById(productId)
        product?.let {
            name = it.name
            description = it.description
            price = it.price.toString()
            category = it.category
            imageUri = it.imageUri?.let { uri -> Uri.parse(uri) }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Product") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
                            Text("Tap to change image")
                        }
                    }
                }
            }
            
            // Input fields
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name *") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                leadingIcon = { Text("$") }
            )
            
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Update button
            Button(
                onClick = {
                    product?.let { currentProduct ->
                        val priceValue = price.toDoubleOrNull() ?: 0.0
                        val updatedProduct = currentProduct.copy(
                            name = name,
                            description = description,
                            price = priceValue,
                            imageUri = imageUri?.toString(),
                            category = category
                        )
                        viewModel.updateProduct(updatedProduct)
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && price.isNotBlank()
            ) {
                Text("Update Product")
            }
        }
    }
}