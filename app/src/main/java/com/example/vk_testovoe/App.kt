package com.example.vk_testovoe

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory

class App : Application(), ImageLoaderFactory {

    val networkMonitor = NetworkMonitor(this)

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this).crossfade(500).build()

}