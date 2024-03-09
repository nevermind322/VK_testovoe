package com.example.vk_testovoe

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vk_testovoe.ui.screens.ProductListScreen
import com.example.vk_testovoe.ui.theme.VK_testovoeTheme
import com.example.vk_testovoe.vm.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainActivity : ComponentActivity() {

    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkMonitor = NetworkMonitor(applicationContext)
        setContent {
            VK_testovoeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(networkMonitor)
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


fun checkOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    return connectivityManager?.activeNetwork != null
}

@Composable
fun MyApp(
    networkMonitor: NetworkMonitor,
    appState: AppState = rememberAppState(networkMonitor = networkMonitor)
) {

    val onlineStatus by appState.isOnline.collectAsStateWithLifecycle()

    if (onlineStatus)
        ProductListScreen()
    else
        Text("Turn on internet")
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
    scope: CoroutineScope = rememberCoroutineScope()
): AppState {
    return remember(scope, networkMonitor) { AppState(networkMonitor, scope) }
}