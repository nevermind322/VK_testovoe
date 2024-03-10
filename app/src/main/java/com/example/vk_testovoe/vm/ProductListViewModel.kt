package com.example.vk_testovoe.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.vk_testovoe.ProductsNetworkPagingSource

class ProductListViewModel : ViewModel() {
    val pagingDataFlow = Pager(PagingConfig(pageSize = 20)) { ProductsNetworkPagingSource() }
        .flow
        .cachedIn(viewModelScope)
}