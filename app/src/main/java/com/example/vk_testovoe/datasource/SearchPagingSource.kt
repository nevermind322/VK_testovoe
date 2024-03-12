package com.example.vk_testovoe.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vk_testovoe.model.Product
import com.example.vk_testovoe.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchPagingSource(private val query: String) : PagingSource<LoadKeys, Product>() {

    override fun getRefreshKey(state: PagingState<LoadKeys, Product>): LoadKeys? = null

    override suspend fun load(params: LoadParams<LoadKeys>): LoadResult<LoadKeys, Product> =
        withContext(Dispatchers.IO) {
            val key = params.key ?: LoadKeys(0, 20)
            try {
                if (query == "")
                    LoadResult.Page(listOf(), null, null)
                else {
                    val response = apiService.searchProducts(query, key.limit, key.skip)
                    val data = response.products
                    LoadResult.Page(
                        data = data,
                        prevKey = key.getPrev(),
                        nextKey = if (response.skip + response.limit >= response.total) null else key.getNext()
                    )
                }
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

}