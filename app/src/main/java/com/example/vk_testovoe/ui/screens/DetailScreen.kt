package com.example.vk_testovoe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.sp
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
            CenteredCircularProgressIndicator()
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
    Column(
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        ProductImagesPager(urlList = product.images)
        Row(verticalAlignment = Alignment.CenterVertically) {
            PriceWithDiscount(
                price = product.price,
                discount = product.discountPercentage,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.width(4.dp))
            Rating(product.rating)
        }
        Text(text = "${product.title}, ${product.brand}")
        Text(text = "Left : ${product.stock}")
        Text(text = product.description, fontSize = 14.sp)
    }
}