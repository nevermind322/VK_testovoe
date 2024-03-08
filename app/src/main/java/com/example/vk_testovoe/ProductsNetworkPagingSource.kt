package com.example.vk_testovoe

import android.media.audiofx.DynamicsProcessing.Limiter
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.network.DummyJsonApiService
import com.example.vk_testovoe.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class LoadKeys(val skip: Int, val limit: Int) {
    fun getNext(): LoadKeys = LoadKeys(skip + limit, limit)
    fun getPrev(): LoadKeys = LoadKeys(skip - limit, limit)
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
            val key = params.key ?: LoadKeys(0, params.loadSize)
            try {
                val data = apiService.getProducts(key.limit, key.skip)
                LoadResult.Page(
                    data = data,
                    prevKey = key.getPrev(),
                    nextKey = key.getNext()
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
}