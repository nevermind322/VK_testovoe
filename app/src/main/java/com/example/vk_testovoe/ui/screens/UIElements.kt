package com.example.vk_testovoe.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vk_testovoe.model.Product
import kotlinx.coroutines.launch

@Composable
fun PriceWithDiscount(price: Int, discount: Double, modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$price $",
            textDecoration = TextDecoration.LineThrough,
            fontSize = 14.sp
        )
        val priceWithDiscount = (price * (100 - discount) / 100)
        val priceString = String.format("%.2f", priceWithDiscount)
        Text(text = "$priceString $", color = Color.Green, fontSize = 14.sp)
    }
}

@Composable
fun Rating(rating: Double) {
    Icon(Icons.Filled.Star, "Star Icon", tint = Color.Yellow)
    Text(text = "$rating")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImagesPager(urlList: List<String>) {

    val pagerState = rememberPagerState { urlList.size }

    Column {
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill,
            modifier = Modifier.height(250.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = urlList[it],
                contentDescription = "Product image",
                modifier = Modifier.fillMaxWidth()
            )
        }

        val scope = rememberCoroutineScope()
        Spacer(modifier = Modifier.size(4.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            for (i in urlList.indices) {
                RadioButton(modifier = Modifier.size(30.dp),
                    selected = i == pagerState.currentPage,
                    onClick = { scope.launch { pagerState.animateScrollToPage(page = i) } })
            }
        }
    }
}

@Composable
fun ProductCard(item: Product, modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    Card(modifier = modifier.clickable { onClick(item.id) }) {
        AsyncImage(
            model = item.thumbnail,
            contentDescription = "Image of ${item.title}",
            contentScale = ContentScale.Fit
        )
        PriceWithDiscount(
            price = item.price,
            discount = item.discountPercentage,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = item.title,
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


@Composable
fun CategoriesMenu(categories: List<String>, curSelectedCategory : String?, onSelectCategory: (String?) -> Unit) {

    var menuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(curSelectedCategory) }

    Button(onClick = { menuExpanded = !menuExpanded }) {
        Text(text ="Chooose category")
        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
            for (category in categories) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    RadioButton(selected = if (selectedCategory == null) false else category == selectedCategory,
                        onClick = {
                            selectedCategory =
                                (if (selectedCategory == category) null else category).also(
                                    onSelectCategory
                                )
                        })
                    Text(text = category, modifier = Modifier.clickable {
                        selectedCategory =
                            (if (selectedCategory == category) null else category).also(
                                onSelectCategory
                            )
                    })
                }
            }
        }
    }
}
