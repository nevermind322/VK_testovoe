package com.example.vk_testovoe.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.vm.ProductListViewModel

@Composable
fun ProductListScreen(vm: ProductListViewModel = viewModel(), onItemClick: (Product) -> Unit) {
    val pagingItems = vm.pagingDataFlow.collectAsLazyPagingItems()
    when (val loadState = pagingItems.loadState.refresh) {
        is LoadState.Loading -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(50.dp)
        ) {
            CircularProgressIndicator()
        }

        is LoadState.Error -> pagingItems.retry()
        else -> ProductList(data = pagingItems, onItemClick = onItemClick)
    }
}

@Composable
fun ProductList(data: LazyPagingItems<Product>, onItemClick: (Product) -> Unit) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp
    ) {
        items(data.itemCount, data.itemKey { it.id }) {
            val product = data[it]
            if (product != null) ProductCard(item = product, onClick = onItemClick)
        }
    }
}

@Composable
fun ProductCard(item: Product, modifier: Modifier = Modifier, onClick: (Product) -> Unit) {
    Card(modifier = modifier.clickable { onClick(item) }) {
        AsyncImage(
            model = item.thumbnail,
            contentDescription = "Image of ${item.title}",
            contentScale = ContentScale.Fit
        )
        Text(
            item.title,
            color = Color.Cyan,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Text(
            text = item.description,
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 14.sp,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
        )
    }
}


