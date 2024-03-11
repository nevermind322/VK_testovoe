package com.example.vk_testovoe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.vm.ProductListViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ProductListScreen(
    snackbarHostState: SnackbarHostState,
    isOnlineFlow: StateFlow<Boolean>,
    onItemClick: (Int) -> Unit,
    vm: ProductListViewModel = viewModel(),
) {
    val isOnline by isOnlineFlow.collectAsStateWithLifecycle()
    var loaded by remember { mutableStateOf(false) }
    val pagingItems = vm.pagingDataFlow.collectAsLazyPagingItems()

    if (!isOnline) {
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar(
                "Please connect to the Internet", duration = SnackbarDuration.Indefinite
            )
        }
    } else if (!loaded) {
        pagingItems.retry()
    }
    when (pagingItems.loadState.refresh) {
        is LoadState.Loading -> Box(
            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }

        is LoadState.Error -> {
            if (isOnline) {
                LaunchedEffect(Unit) {
                    val res = snackbarHostState.showSnackbar(
                        "Something went wrong",
                        actionLabel = "retry"
                    )
                    when (res) {
                        SnackbarResult.ActionPerformed -> pagingItems.retry()
                        else -> Unit
                    }
                }
            }
        }
        else -> {
            loaded = true
            ProductList(data = pagingItems, onItemClick = onItemClick)
        }
    }
}

@Composable
fun ProductList(data: LazyPagingItems<Product>, onItemClick: (Int) -> Unit) {
    if (data.loadState.prepend is LoadState.Error)
        data.retry()
    if (data.loadState.append is LoadState.Error)
        data.retry()

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
