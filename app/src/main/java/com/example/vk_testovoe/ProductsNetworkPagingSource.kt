package com.example.vk_testovoe

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.network.DummyJsonApiService
import com.example.vk_testovoe.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class LoadKeys(val skip: Int, val limit: Int) {
    fun getNext(): LoadKeys = LoadKeys(skip + limit, limit)
    fun getPrev(): LoadKeys? = if (skip == 0) null else LoadKeys(skip - limit, limit)
}

class ProductsNetworkPagingSource(dummyJsonApiService: DummyJsonApiService = apiService) :
    PagingSource<LoadKeys, Product>() {
    override fun getRefreshKey(state: PagingState<LoadKeys, Product>): LoadKeys? {
        val anchor = state.anchorPosition ?: return null

        val last = state.closestItemToPosition(anchor) ?: return null
        return LoadKeys(last.id, state.config.pageSize)
    }

    override suspend fun load(params: LoadParams<LoadKeys>): LoadResult<LoadKeys, Product> =
        withContext(Dispatchers.IO) {
            val key = params.key ?: LoadKeys(0, 20)
            try {
                val data = apiService.getProducts(key.limit, key.skip).products
                LoadResult.Page(
                    data = data,
                    prevKey = key.getPrev(),
                    nextKey = if (data.isEmpty()) null else key.getNext()
                )
            } catch (e: Exception) {
                Log.d("paging", e.javaClass.name)
                LoadResult.Error(e)
            }
        }
}