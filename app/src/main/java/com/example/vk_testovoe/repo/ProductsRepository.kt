package com.example.vk_testovoe.repo

import com.example.vk_testovoe.network.ApiResult
import com.example.vk_testovoe.network.DummyJsonApiService
import com.example.vk_testovoe.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductsRepository(private val api: DummyJsonApiService = apiService) {

    suspend fun getProduct(id: Int) = withContext(Dispatchers.IO) {
        try {
            val res = api.getProduct(id)
            ApiResult.Success(res)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }
}

