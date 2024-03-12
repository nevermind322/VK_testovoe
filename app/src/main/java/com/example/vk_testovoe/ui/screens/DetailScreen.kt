package com.example.vk_testovoe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.vm.DetailScreenState
import com.example.vk_testovoe.vm.DetailViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    id: Int,
    snackbarHostState: SnackbarHostState,
    isOnlineFlow: StateFlow<Boolean>,
    vm: DetailViewModel = viewModel()
) {
    var state: DetailScreenState by remember { mutableStateOf(DetailScreenState.Loading) }
    val isOnline by isOnlineFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    LaunchedEffect(isOnline) {
        state = DetailScreenState.Loading
        if (isOnline) launch {
            state = vm.getProduct(id)
        }
    }
    if (!isOnline) {
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar(
                "Please connect to the Internet", duration = SnackbarDuration.Indefinite
            )
        }
    } else when (state) {
        DetailScreenState.Loading -> {
            CenteredCircularProgressIndicator()
        }

        is DetailScreenState.Success -> {
            DetailScreen((state as DetailScreenState.Success).product)
        }

        is DetailScreenState.Error -> {
            Column {
                Text(text = "Something went wrong!")
                Button(onClick = { scope.launch { state = vm.getProduct(id) } }) {
                    Text(text = "retry")
                }
            }
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
                price = product.price, discount = product.discountPercentage, modifier = Modifier
            )
            Spacer(modifier = Modifier.width(4.dp))
            Rating(product.rating)
        }
        Text(text = "${product.title}, ${product.brand}")
        Text(text = "Left : ${product.stock}")
        Text(text = product.description, fontSize = 14.sp)
    }
}