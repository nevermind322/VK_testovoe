package com.example.vk_testovoe.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.network.ApiResult
import com.example.vk_testovoe.repo.ProductsRepository

class DetailViewModel(val repo: ProductsRepository = ProductsRepository()) : ViewModel() {

    suspend fun getProduct(id: Int) = when (val res = repo.getProduct(id)) {
        is ApiResult.Success -> DetailScreenState.Success(res.data)
        is ApiResult.Error -> DetailScreenState.Error(res.msg)
            .also { Log.d("products", "error: ${res.msg}") }
    }

}

sealed class DetailScreenState() {
    data object Loading : DetailScreenState()
    data class Success(val product: Product) : DetailScreenState()
    data class Error(val msg: String) : DetailScreenState()
}
