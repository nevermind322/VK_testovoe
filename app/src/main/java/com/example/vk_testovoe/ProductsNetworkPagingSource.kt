package com.example.vk_testovoe

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class LoadKeys(val skip: Int, val limit: Int) {
    fun getNext(): LoadKeys = LoadKeys(skip + limit, limit)
    fun getPrev(): LoadKeys? = if (skip < limit) null else LoadKeys(skip - limit, limit)
}

class ProductsNetworkPagingSource(private val category: String?) :
    PagingSource<LoadKeys, Product>() {
    override fun getRefreshKey(state: PagingState<LoadKeys, Product>): LoadKeys? = null

    override suspend fun load(params: LoadParams<LoadKeys>): LoadResult<LoadKeys, Product> =
        withContext(Dispatchers.IO) {
            val key = params.key ?: LoadKeys(0, 20)
            try {
                val response = if (category == null)
                    apiService.getProducts(key.limit, key.skip)
                else
                    apiService.getProductsInCategory(category, key.limit, key.skip)
                val data = response.products
                LoadResult.Page(
                    data = data,
                    prevKey = key.getPrev(),
                    nextKey = if (response.skip + response.limit >= response.total) null else key.getNext()
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
}