package com.example.vk_testovoe.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.vm.ProductListViewModel

@Composable
fun ProductListScreen(vm: ProductListViewModel = viewModel()) {
    val data = vm.flow.collectAsLazyPagingItems()
    ProductList(data = data)
}

@Composable
fun ProductList(data: LazyPagingItems<Product>) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.padding(horizontal = 4.dp)) {
        items(data.itemCount, data.itemKey { it.id }) {
            val product = data[it]
            if (product != null)
                ProductCard(product)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ProductCard(item: Product) {
    Column(
        modifier = Modifier
            //.height(400.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = item.thumbnail,
            contentDescription = "null",
            modifier = Modifier
                .height(300.dp)
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(item.title, color = Color.Cyan, fontSize = 18.sp)
        Text(
            text = item.description,
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 14.sp
        )
    }
}
