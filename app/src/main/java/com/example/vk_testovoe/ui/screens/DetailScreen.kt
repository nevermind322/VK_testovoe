package com.example.vk_testovoe.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.vm.DetailScreenState
import com.example.vk_testovoe.vm.DetailViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(id: Int, vm: DetailViewModel = viewModel()) {
    var state: DetailScreenState by remember { mutableStateOf(DetailScreenState.Loading) }

    LaunchedEffect(Unit) {
        launch {
            state = vm.getProduct(id)
        }
    }
    when (state) {
        DetailScreenState.Loading -> {
            Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailScreenState.Success -> {
            DetailScreen((state as DetailScreenState.Success).product)
        }

        is DetailScreenState.Error -> {
            Text(text = "Error ${(state as DetailScreenState.Error).msg}")
        }
    }
}

@Composable
internal fun DetailScreen(product: Product) {
    Text(text = product.title)
}