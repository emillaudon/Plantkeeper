package com.example.plantkeeper.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.plantkeeper.models.PlantSorter.Companion.bubbleSort
import javax.security.auth.callback.Callback
import kotlin.concurrent.thread

class PlantHandler {
    val networkHandler = NetworkHandler()
    var plantList = emptyArray<Plant>()

    @RequiresApi(Build.VERSION_CODES.N)
    fun createNewPlant(newPost: Plant) {
        networkHandler.newPlant(newPost, newPost.image)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getUserPlants(callback: () -> Unit) {

        thread {
            networkHandler.getUserPosts {
                if (it.count() > plantList.count()) {
                    plantList = it.toTypedArray()
                    plantList = plantList.bubbleSort()
                }
                callback()
            }
        }.run()
    }
}