package com.example.vk_testovoe.network

import com.example.vk_testovoe.model.Category
import com.example.vk_testovoe.model.Product
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


internal const val BASE_URL = "https://dummyjson.com/"

val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

val retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

val apiService = retrofit.create(DummyJsonApiService::class.java)

interface DummyJsonApiService {

    @GET("products")
    suspend fun getProducts(@Query("limit") limit: Int, @Query("skip") skip: Int): ApiResponse

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product

    @GET("product/categories")
    suspend fun getCategories(): List<Category>

    @GET("products/category/{category}")
    suspend fun getProductsInCategory(
        @Path("category") category: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ApiResponse

    @GET("/products/search")
    suspend fun searchProducts(
        @Query("q") q: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ApiResponse

}

data class ApiResponse(val products: List<Product>, val total: Int, val skip: Int, val limit: Int)