package com.example.vk_testovoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vk_testovoe.ui.screens.DetailScreen
import com.example.vk_testovoe.ui.screens.ProductListScreen
import com.example.vk_testovoe.ui.theme.VK_testovoeTheme
import com.example.vk_testovoe.vm.CategoriesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainActivity : ComponentActivity() {

    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkMonitor = NetworkMonitor(applicationContext)
        setContent {
            VK_testovoeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(networkMonitor)
                }
            }
        }
    }
}


@Composable
fun MyApp(
    networkMonitor: NetworkMonitor,
    appState: AppState = rememberAppState(networkMonitor = networkMonitor),
) {
    val navController = rememberNavController()
    MainNavHost(navController, appState)
}

const val DETAIL_ROUTE = "detail"
const val PRODUCT_LIST_ROUTE = "productList"
const val DETAIL_PARAMETER = "id"

@Composable
fun MainNavHost(
    navController: NavHostController, appState: AppState, vm: CategoriesViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = PRODUCT_LIST_ROUTE
        ) {

            composable(PRODUCT_LIST_ROUTE) {
                ProductListScreen(
                    isOnlineFlow = appState.isOnline,
                    snackbarHostState = snackbarHostState,
                    onItemClick = { id -> navController.navigate("$DETAIL_ROUTE/$id") },
                )
            }
            composable("$DETAIL_ROUTE/{$DETAIL_PARAMETER}") {
                DetailScreen(id = it.arguments!!.getString(DETAIL_PARAMETER)!!.toInt())
            }
        }
    }
}


@Stable
class AppState(networkMonitor: NetworkMonitor, scope: CoroutineScope) {
    val isOnline = networkMonitor.isOnline.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true,
    )
}

@Composable
fun rememberAppState(
    networkMonitor: NetworkMonitor,
    scope: CoroutineScope = rememberCoroutineScope(),
): AppState {
    return remember(scope, networkMonitor) { AppState(networkMonitor, scope) }
}