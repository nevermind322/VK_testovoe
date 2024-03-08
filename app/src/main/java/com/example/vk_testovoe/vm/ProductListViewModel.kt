package com.example.vk_testovoe.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.vk_testovoe.ProductsNetworkPagingSource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {
    val flow = Pager(PagingConfig(pageSize = 20)) { ProductsNetworkPagingSource() }.flow.cachedIn(
        viewModelScope
    )
}