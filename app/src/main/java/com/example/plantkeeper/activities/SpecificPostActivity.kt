package com.example.plantkeeper.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = plant.name

        recyclerView = findViewById<RecyclerView>(R.id.updatesRecyclerView)

        recyclerView.adapter = UpdateAdapter(this, plant.plantUpdates.reversed())
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //var image = findViewById<ImageView>(R.id.imageViewUpdate)
        //Picasso.get().load(plant.image).into(imageViewPlant);

        var wateringCircle = findViewById<ProgressBar>(R.id.progressBarWateringSpecific)
        var sunlightCircle = findViewById<ProgressBar>(R.id.progressBarSunlightSpecific)
        var temperatureCircle = findViewById<ProgressBar>(R.id.progressBarTemperatureSpecific)

        wateringCircle.progress = plant.wateringFreq
        sunlightCircle.progress = plant.wateringFreq
        temperatureCircle.progress = plant.temperature

        var noteTextview = findViewById<TextView>(R.id.noteSpecific)

        noteTextview.text = plant.note

        //var heightText = findViewById<TextView>(R.id.heightSpecific)
        //var height = plant.height
        //heightText.text = "$height CM"

        var updateButton = findViewById<ImageView>(R.id.newUpdateButton)
        updateButton.setOnClickListener {
            var intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("plant", plant)
            startActivityForResult(intent, 1)
           //startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                ////Use object
                if (data != null) {
                    val result = data.getSerializableExtra("result") as PlantUpdate
                    plant.plantUpdates = plant.plantUpdates + result

                    println(plant.plantUpdates.count())

                    recyclerView.adapter = UpdateAdapter(this, plant.plantUpdates.reversed())
                }

            } else {
                ///Do nothing
                ///TODO: Add error message?
            }
        }
    }
}