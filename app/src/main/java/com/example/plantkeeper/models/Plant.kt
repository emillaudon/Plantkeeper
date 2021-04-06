package com.example.plantkeeper.models

import android.graphics.Bitmap

class Plant(val image: Bitmap, val name: String, var wateringFreq: Int, var temperature: Int, var sunlight: Int, var note: String) {

}