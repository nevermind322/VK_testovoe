package com.example.vk_testovoe.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.vm.ProductListViewModel

@Composable
fun ProductListScreen(vm: ProductListViewModel = viewModel()) {
    val data = vm.flow.collectAsLazyPagingItems()
    ProductList(data = data)
}

@Composable
fun ProductList(data: LazyPagingItems<Product>) {
    LazyColumn {
        items(data.itemCount, data.itemKey { it.id }) {
            val product = data[it]
            if (product != null)
                ProductItem(product)
        }
    }
}

@Composable
fun ProductItem(item: Product) {
    Row {
        Text(item.title, color = Color.Cyan)
    }
}
