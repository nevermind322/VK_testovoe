package com.example.vk_testovoe.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.vk_testovoe.datasource.ProductsNetworkPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductListViewModel : ViewModel() {

    private val _category = MutableStateFlow<String?>(null)

    val category = _category.asStateFlow()

    val pagingDataFlow =
        Pager(PagingConfig(pageSize = 20)) { ProductsNetworkPagingSource(category.value) }
            .flow
            .cachedIn(viewModelScope)


    fun updateCategory(category: String) {
        _category.update { if (it == category) null else category }

    }

}