package com.example.vk_testovoe.network

import com.example.vk_testovoe.model.Product
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


internal const val BASE_URL = "https://jsonformatter.org"

val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

val retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

val apiService = retrofit.create(DummyJsonApiService::class.java)

interface DummyJsonApiService {

    @GET("/products")
    fun getProducts(@Query("limit") limit: Int, @Query("skip") skip: Int): List<Product>

}