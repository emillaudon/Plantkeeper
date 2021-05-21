package com.example.plantkeeper.models

import java.io.Serializable

class PlantUpdate(var height: Double, var image: String, var note: String, var daysOld: String, var timeCreated: Int, var creatorName: String) : Serializable {
}