package com.example.plantkeeper.models

import android.graphics.Bitmap
import java.io.Serializable

class Plant(val image: String, val name: String, var wateringFreq: Int, var temperature: Int, var sunlight: Int, var note: String, var height: Double, var plantId: String, var plantUpdates: List<PlantUpdate>, var creationTime: Int) : Serializable {

}