package com.example.vk_testovoe.network

sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val msg: String) : ApiResult<Nothing>()
}