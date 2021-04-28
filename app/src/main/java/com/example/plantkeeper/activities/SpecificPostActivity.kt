package com.example.plantkeeper.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.plantkeeper.R
import com.example.plantkeeper.models.Plant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_specific_post.*

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
    }
}