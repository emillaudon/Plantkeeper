package com.example.plantkeeper.models

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images
import androidx.annotation.RequiresApi

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL


class NetworkHandler {
    val postUrl = "https://us-central1-plantkeeper-44769.cloudfunctions.net/post"

    @RequiresApi(Build.VERSION_CODES.N)
    fun get() {
        val url = URL(postUrl + "/IR80C7GlspczdTtdhAWFKE9sSvp2")
        val auth = Firebase.auth

        val user = auth.currentUser

        user.getIdToken(true)
            .addOnSuccessListener { result ->
                val idToken = result.token
                //Do whatever
                println("GetTokenResult result = $idToken")
                println("kkkk")

                val bearerToken = idToken

                val thread = Thread(Runnable {
                    try {

                        val connection = url.openConnection()
                        connection.setRequestProperty("Bearer", bearerToken)

                        with(url.openConnection() as HttpURLConnection) {
                            requestMethod = "GET"  // optional default is GET
                            setRequestProperty("Authorization","Bearer "+ bearerToken)

                            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

                            inputStream.bufferedReader().use {
                                it.lines().forEach { line ->
                                    println('y')
                                    println(line)
                                }
                            }
                        }

                    }
                    catch (e:Exception) {
                        println(e)

                    }
                })
                thread.start()

            }




        println("ffff")


    }

    fun newPost(post: Post, imgPath: String) {
        val storage = FirebaseStorage.getInstance();
        val storageRef = storage.getReference();

        var file = Uri.fromFile(File(imgPath))
        val ref = storageRef.child("images/${file.lastPathSegment}")
        val uploadTask = ref.putFile(file)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                println(downloadUri)
            } else {

            }
        }

    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
}