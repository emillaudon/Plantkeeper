package com.example.plantkeeper.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.plantkeeper.R
import com.example.plantkeeper.models.NetworkHandler

class NewUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        var saveButton = findViewById<Button>(R.id.saveUserNameButton)

        saveButton.setOnClickListener {
            var userNameEditText = findViewById<EditText>(R.id.userNameEditText)
            var userName = userNameEditText.text.toString()

            ///TODO: Put name into user object

            ///TODO: Save name to database
            var networkHandler = NetworkHandler()
            networkHandler.saveUserName(userName) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}