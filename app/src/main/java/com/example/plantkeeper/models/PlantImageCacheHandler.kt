package com.example.plantkeeper.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import java.lang.Exception
import java.net.URL

object PlantImageCacheHandler {
    private lateinit var memoryCache: LruCache<String, Bitmap>

    fun createCache() {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val sizeOfCache = maxMemory / 16

        memoryCache = object : LruCache<String, Bitmap>(sizeOfCache) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                println(bitmap.byteCount)
                println("size")
                return bitmap.byteCount / 1024
            }
        }
    }

    fun cacheImage(urlString: String) {
        if (!this::memoryCache.isInitialized) {
            println("not init")
            return
        }
        println("init")

            val thread = Thread(Runnable {

                val imageUrl = URL(urlString)

                val imgStream = imageUrl.openStream()
                val image = BitmapFactory.decodeStream(imgStream)

                try {
                    memoryCache.put("plantImage", image)
                } catch(e: Exception) {
                    println(e)
                }
                println("!")
                println(isCached())
            })
            thread.start()
    }


    fun getImage(): Bitmap?  {

        if (!this::memoryCache.isInitialized) {
            return null
        }
        println("aha")

        var bitmap: Bitmap? = null

        bitmap = memoryCache.get("plantImage")

        return bitmap
    }

    fun isCached(): Boolean {
        println(memoryCache.snapshot().values)
        if (memoryCache.snapshot().values.count() == 0) return false

        return true
    }



}