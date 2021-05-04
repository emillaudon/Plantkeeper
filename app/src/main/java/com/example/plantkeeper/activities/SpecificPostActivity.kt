package com.example.plantkeeper.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.plantkeeper.R
import com.example.plantkeeper.models.Plant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_specific_post.*
import org.w3c.dom.Text

class SpecificPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_post)
        val plant: Plant = intent.getSerializableExtra("plant") as Plant
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = plant.name

        var image = findViewById<ImageView>(R.id.imageViewPlant)
        Picasso.get().load(plant.image).into(imageViewPlant);

        var wateringCircle = findViewById<ProgressBar>(R.id.progressBarWateringSpecific)
        var sunlightCircle = findViewById<ProgressBar>(R.id.progressBarSunlightSpecific)
        var temperatureCircle = findViewById<ProgressBar>(R.id.progressBarTemperatureSpecific)

        wateringCircle.progress = plant.wateringFreq
        sunlightCircle.progress = plant.wateringFreq
        temperatureCircle.progress = plant.temperature

        var noteTextview = findViewById<TextView>(R.id.noteSpecific)

        noteTextview.text = plant.note
    }
}