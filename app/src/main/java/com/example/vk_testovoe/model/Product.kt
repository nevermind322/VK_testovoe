package com.example.vk_testovoe.model

import androidx.compose.runtime.Stable

@Stable
data class Product(
    val id: Int,
    val title: String,
    val description : String,
    val price: Int,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
)