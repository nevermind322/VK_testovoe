package com.example.vk_testovoe

import com.example.vk_testovoe.network.ApiResult
import com.example.vk_testovoe.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CategoryRepo {
    suspend fun getCategories() = withContext(Dispatchers.IO) {
        try {
            val res = apiService.getCategories()
            ApiResult.Success(res)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

}