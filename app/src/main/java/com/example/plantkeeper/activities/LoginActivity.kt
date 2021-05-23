package com.example.plantkeeper.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.plantkeeper.R
import com.example.plantkeeper.models.NetworkHandler
import com.example.plantkeeper.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth

    lateinit var emailField: EditText
    lateinit var passwordField: EditText

    lateinit var sharedPref: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        isConnected()

        sharedPref = getSharedPreferences("userMail", Context.MODE_PRIVATE)
        auth = Firebase.auth

        var loginButton = findViewById<Button>(R.id.loginButton)
        emailField = findViewById<EditText>(R.id.emailEditText)
        passwordField = findViewById<EditText>(R.id.passwordEditText)

        //Check sharedPreferences for cached email, set emailfield to it if exists, else set it to empty.
        emailField.setText((sharedPref.getString("userEmail", "")))



        var registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            if (isConnected()) {
                registerButtonClicked()
            }
        }

        loginButton.setOnClickListener {
            if (isConnected()) {
                loginButtonClicked()
            }
        }
    }

    private fun isConnected() : Boolean {
        if (!NetworkHandler.isOnline(this)) {
            Toast.makeText(baseContext, "You need to be connected to the internet to use this application.", Toast.LENGTH_LONG)
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    user.getIdToken(true)
                        .addOnSuccessListener { result ->
                            sharedPref.edit().putString("userEmail", emailField.text.toString()).apply()

                            User(false) {
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                        }
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

            }

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun loginButtonClicked() {
        var email = emailField.text.toString()
        var password = passwordField.text.toString()
        if (credentialsFilledOut()) {
            login(email, password)
        } else {
            Toast.makeText(this, "Make sure that both fields are filled out.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerButtonClicked() {
        var email = emailField.text.toString()
        var password = passwordField.text.toString()
        if (credentialsFilledOut()) {
            auth.createUserWithEmailAndPassword(email, password)
            startActivity(Intent(this, NewUserActivity::class.java))
        } else {
            Toast.makeText(this, "Make sure that both fields are filled out.", Toast.LENGTH_SHORT).show()
        }
    }

    fun credentialsFilledOut() : Boolean {
        return emailField.text.toString() != "" && passwordField.text.toString() != ""
    }
}
