package com.example.vk_testovoe.model

data class Product(
    val id: Int,
    val title: String,
    val price: Int,
    val discountPercentage: Int,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
) {
}