package com.example.plantkeeper.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantkeeper.R
import com.example.plantkeeper.adapters.UpdateAdapter
import com.example.plantkeeper.models.Plant
import com.example.plantkeeper.models.PlantUpdate
import com.squareup.picasso.Picasso

class SpecificPostActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    lateinit var plant: Plant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_post)

        plant = intent.getSerializableExtra("plant") as Plant

        var title = findViewById<TextView>(R.id.plantNameText)
        title.text = plant.name

        var backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        recyclerView = findViewById<RecyclerView>(R.id.updatesRecyclerView)
        recyclerView.adapter = UpdateAdapter(this, plant.plantUpdates.reversed())
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        var wateringCircle = findViewById<ProgressBar>(R.id.progressBarWateringSpecific)
        var sunlightCircle = findViewById<ProgressBar>(R.id.progressBarSunlightSpecific)
        var temperatureCircle = findViewById<ProgressBar>(R.id.progressBarTemperatureSpecific)

        wateringCircle.progress = plant.wateringFreq
        sunlightCircle.progress = plant.wateringFreq
        temperatureCircle.progress = plant.temperature

        var noteTextview = findViewById<TextView>(R.id.noteSpecific)

        noteTextview.text = plant.note

        var updateButton = findViewById<ImageView>(R.id.newUpdateButton)
        updateButton.setOnClickListener {
            var intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("plant", plant)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                ////Use object
                if (data != null) {
                    updateWithNewData(data)
                }
            }
        }
    }

    private fun updateWithNewData(data: Intent) {
        val result = data.getSerializableExtra("result") as PlantUpdate
        plant.height = result.height
        plant.plantUpdates = plant.plantUpdates + result

        recyclerView.adapter = UpdateAdapter(this, plant.plantUpdates.reversed())
    }
}