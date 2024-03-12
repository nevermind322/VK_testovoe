package com.example.vk_testovoe.ui.screens

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.vm.SearchScreenViewModel
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Flow

@Composable
fun SearchScreen(
    snackbarHostState: SnackbarHostState,
    isOnlineFlow: StateFlow<Boolean>,
    pagingItems: LazyPagingItems<Product>,
    onItemClick: (Int) -> Unit,
) {


    val isOnline by isOnlineFlow.collectAsStateWithLifecycle()
    var loaded by remember { mutableStateOf(false) }

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
        is LoadState.Loading -> {
            CenteredCircularProgressIndicator()
        }

        is LoadState.Error -> {
            if (isOnline) {
                LaunchedEffect(Unit) {
                    val res = snackbarHostState.showSnackbar(
                        "Something went wrong", actionLabel = "retry"
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
            ProductList(
                data = pagingItems,
                onItemClick = onItemClick,
            )
        }
    }
}