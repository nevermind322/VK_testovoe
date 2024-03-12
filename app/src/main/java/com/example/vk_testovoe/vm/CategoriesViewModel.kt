package com.example.vk_testovoe.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_testovoe.repo.CategoryRepo
import com.example.vk_testovoe.network.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    private val _categoriesFlow = MutableStateFlow<AppUiState>(AppUiState.Loading)
    
    val categoriesFlow = _categoriesFlow.asStateFlow()
    fun loadCategories() {
        viewModelScope.launch {
            when (val res = CategoryRepo.getCategories()) {
                is ApiResult.Success -> _categoriesFlow.value = AppUiState.Success(res.data)
                is ApiResult.Error -> _categoriesFlow.value = AppUiState.Error(res.msg)
            }
        }
    }
}

sealed class AppUiState {
    data object Loading : AppUiState()
    data class Success(val categories: List<String>) : AppUiState()
    data class Error(val msg: String) : AppUiState()

}