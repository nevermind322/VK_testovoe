package com.example.vk_testovoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.vk_testovoe.ui.screens.CategoriesMenu
import com.example.vk_testovoe.ui.screens.CenteredCircularProgressIndicator
import com.example.vk_testovoe.ui.screens.DetailScreen
import com.example.vk_testovoe.ui.screens.ProductListScreen
import com.example.vk_testovoe.ui.screens.SearchScreen
import com.example.vk_testovoe.ui.theme.VK_testovoeTheme
import com.example.vk_testovoe.vm.AppUiState
import com.example.vk_testovoe.vm.CategoriesViewModel
import com.example.vk_testovoe.vm.ProductListViewModel
import com.example.vk_testovoe.vm.SearchScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
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
const val SEARCH_ROUTE = "search"

@Composable
fun MainNavHost(
    navController: NavHostController, appState: AppState, vm: CategoriesViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val searchVm: SearchScreenViewModel = viewModel()
    val productListVM: ProductListViewModel = viewModel()


    val searchPagingItems = searchVm.pagingDataFlow.collectAsLazyPagingItems()
    val productListPagingItems = productListVM.pagingDataFlow.collectAsLazyPagingItems()
    val selectedCategory by productListVM.category.collectAsStateWithLifecycle()


    val isOnline by appState.isOnline.collectAsStateWithLifecycle()
    val state by vm.categoriesFlow.collectAsState()
    LaunchedEffect(Unit) {
        vm.loadCategories()
    }
Text("hello")
    when (state) {
        is AppUiState.Loading -> CenteredCircularProgressIndicator()
        is AppUiState.Error -> {
            if (!isOnline) {
                LaunchedEffect(Unit) {
                    snackbarHostState.showSnackbar(
                        "Please connect to the Internet", duration = SnackbarDuration.Indefinite
                    )
                }
            } else {
                LaunchedEffect(Unit) {
                    delay(1_000)
                    vm.loadCategories()
                }
            }
        }

        is AppUiState.Success -> {
            val categories = (state as AppUiState.Success).categories
            Text("s")
            Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
                AppBar(onSearchClick = {
                    searchVm.q = ""
                    searchPagingItems.refresh()
                    navController.navigate(SEARCH_ROUTE)
                }, onSearch = {
                    searchVm.q = it
                    searchPagingItems.refresh()
                }, navController = navController, categoriesMenu = {
                    CategoriesMenu(categories = categories,
                        selectedCategory = selectedCategory,
                        onSelectCategory = {
                            productListVM.updateCategory(it.name)
                            productListPagingItems.refresh()
                        })
                })
            }) {
                NavHost(
                    modifier = Modifier.padding(it),
                    navController = navController,
                    startDestination = PRODUCT_LIST_ROUTE
                ) {

                    composable(SEARCH_ROUTE) {
                        SearchScreen(snackbarHostState = snackbarHostState,
                            isOnlineFlow = appState.isOnline,
                            pagingItems = searchPagingItems,
                            onItemClick = { id -> navController.navigate("$DETAIL_ROUTE/$id") })
                    }
                    composable(PRODUCT_LIST_ROUTE) {
                        ProductListScreen(
                            snackbarHostState = snackbarHostState,
                            isOnlineFlow = appState.isOnline,
                            onItemClick = { id -> navController.navigate("$DETAIL_ROUTE/$id") },
                            pagingItems = productListPagingItems
                        )
                    }
                    composable("$DETAIL_ROUTE/{$DETAIL_PARAMETER}") {
                        DetailScreen(
                            id = it.arguments!!.getString(DETAIL_PARAMETER)!!.toInt(),
                            snackbarHostState = snackbarHostState,
                            isOnlineFlow = appState.isOnline
                        )
                    }
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onSearchClick: () -> Unit,
    onSearch: (String) -> Unit,
    navController: NavHostController,
    categoriesMenu: @Composable () -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    var q by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchWithHideKeyboard: (String) -> Unit = {
        onSearch(it)
        keyboardController?.hide()
    }

    TopAppBar(title = { Text(text = "DummyJson") },
        modifier = Modifier.padding(horizontal = 4.dp),
        navigationIcon = {
            Icon(imageVector = Icons.Filled.ArrowBack,
                contentDescription = "return back",
                modifier = Modifier.clickable { navController.navigateUp() })
        },
        actions = {
            if (backStackEntry?.destination?.route == SEARCH_ROUTE) {
                TextField(
                    value = q,
                    onValueChange = { q = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearchWithHideKeyboard(q) }),
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Filled.Search,
                    contentDescription = "Search icon",
                    modifier = Modifier.clickable { onSearchWithHideKeyboard(q) })
            } else if (backStackEntry?.destination?.route == PRODUCT_LIST_ROUTE) {
                categoriesMenu()
                Icon(imageVector = Icons.Filled.Search,
                    contentDescription = "Search icon",
                    modifier = Modifier.clickable {
                        q = ""
                        onSearchClick()
                    })
            }
        })
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