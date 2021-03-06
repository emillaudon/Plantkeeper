package com.example.plantkeeper.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.example.plantkeeper.R
import com.example.plantkeeper.models.NetworkHandler
import com.example.plantkeeper.models.User

class NewUserActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        var saveButton = findViewById<Button>(R.id.saveUserNameButton)

        saveButton.setOnClickListener {
            var userNameEditText = findViewById<EditText>(R.id.userNameEditText)
            var userName = userNameEditText.text.toString()

            User(true, userName) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}