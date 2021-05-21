package com.example.plantkeeper.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.concurrent.TimeUnit

class UpdateHandler {
    var networkHandler = NetworkHandler()

    var list = ArrayList<PlantUpdate>()

    @RequiresApi(Build.VERSION_CODES.N)
    fun newUpdate(plant: Plant, currentPhotoPath: String, note: String, callback: (PlantUpdate) -> Unit) {
        val currentTime = System.currentTimeMillis()

        val millionSeconds = (plant.creationTime.toLong() * 1000) - currentTime
        var daysSinceCreation = TimeUnit.MILLISECONDS.toDays(millionSeconds).toString()
        if (daysSinceCreation.contains("-")) {
            daysSinceCreation = daysSinceCreation.split("-")[1]
        }

        var timeCreated = (currentTime / 1000).toInt()

        var update = PlantUpdate(plant.height, "imageUrl", note, daysSinceCreation, timeCreated, User.name)

        networkHandler.newUpdate(currentPhotoPath, plant, update) {imageUrl ->
            update.image = imageUrl
            callback(update)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getFriendUpdates(callback: () -> Unit) {
        networkHandler.getFriendPosts() {
            if (it.count() > 0 && list != ArrayList(it)) {
                list = ArrayList(it)
            }
            callback()
        }
    }
}