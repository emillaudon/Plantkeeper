package com.example.plantkeeper.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.plantkeeper.R
import com.example.plantkeeper.models.NetworkHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.thread


class LoginActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth;

        setContentView(R.layout.activity_login)

        var loginButton = findViewById<Button>(R.id.button);

        loginButton.setOnClickListener {
            login("testmail2@email.test", "testtest23");
        }

        var registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            var emailField = findViewById<EditText>(R.id.emailEditText)
            var passwordField = findViewById<EditText>(R.id.passwordEditText)
            auth.createUserWithEmailAndPassword(emailField.text.toString(), passwordField.text.toString())
            startActivity(Intent(this, NewUserActivity::class.java))
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    println("signInWithEmail:success");
                    val user = auth.currentUser
                    user.getIdToken(true)
                        .addOnSuccessListener { result ->
                            val idToken = result.token
                            //Do whatever
                            println("GetTokenResult result = $idToken")


                            ///TODO: Put name into user object

                            ///TODO: Save to database

                            startActivity(Intent(this, MainActivity::class.java))
                }

                } else {
                    // If sign in fails, display a message to the user.
                    println("signInWithEmail:failure")
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                    // ...
                }

                // ...
            }

    }
}
