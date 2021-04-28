package com.example.plantkeeper.models

import android.graphics.Bitmap
import java.io.Serializable

class Plant(val image: String, val name: String, var wateringFreq: Int, var temperature: Int, var sunlight: Int, var note: String) : Serializable {

}