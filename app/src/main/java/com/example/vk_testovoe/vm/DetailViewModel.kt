package com.example.vk_testovoe.vm

import androidx.lifecycle.ViewModel
import com.example.vk_testovoe.network.ApiResult
import com.example.vk_testovoe.ProductsRepository
import com.example.vk_testovoe.model.Product

class DetailViewModel(val repo: ProductsRepository = ProductsRepository()) : ViewModel() {

    suspend fun getProduct(id: Int) = when (val res = repo.getProduct(id)) {
        is ApiResult.Success -> DetailScreenState.Success(res.data)
        is ApiResult.Error -> DetailScreenState.Error(res.msg)
    }

}

sealed class DetailScreenState() {
    data object Loading : DetailScreenState()
    data class Success(val product: Product) : DetailScreenState()
    data class Error(val msg: String) : DetailScreenState()
}
