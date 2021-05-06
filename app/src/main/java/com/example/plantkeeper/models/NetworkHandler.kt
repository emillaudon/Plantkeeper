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
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStreamWriter
import java.lang.Error
import java.net.HttpURLConnection
import java.net.URL


class NetworkHandler {
    val postUrl = "https://us-central1-plantkeeper-44769.cloudfunctions.net/post"

    @RequiresApi(Build.VERSION_CODES.N)
    fun getUserPosts(callback: (result: List<Plant>) -> Unit) {
        val auth = Firebase.auth

        val user = auth.currentUser

        val url = URL(postUrl + "/" + user.uid)

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
                                var plantsList = mutableListOf<Plant>()
                                it.lines().forEach { line ->
                                    println(line)
                                    var array = JSONArray(line)
                                    for (i in 0 until array.length()) {
                                        val jsonObject = array.getJSONObject(i)
                                        //Plant(val image: String, val name: String, var wateringFreq: Int, var temperature: Int, var sunlight: Int, var note: String) {
                                        var imageString = jsonObject["imageUrl"] as String
                                        imageString = imageString.replace("\\/", "/")

                                        var height: Double = -10.0
                                        print("fffffffffff")

                                        try {
                                            var intHeight = jsonObject["height"] as Int
                                            println("1234")
                                            height = intHeight.toDouble() / 10
                                            println("12345")
                                        } catch (e: Error) {

                                        }
                                        println("12345")

                                        var plantUpdatesJson = jsonObject["updates"] as JSONArray
                                        var plantUpdates = plantUpdatesFromJson(plantUpdatesJson)


                                        val plantFromJson = Plant(
                                            imageString,
                                            jsonObject["title"] as String,
                                            jsonObject["watering"] as Int,
                                            jsonObject["temperature"] as Int,
                                            jsonObject["sunlight"] as Int,
                                            jsonObject["note"] as String,
                                            height,
                                            jsonObject["id"] as String,
                                            plantUpdates)

                                        plantsList.add(plantFromJson)
                                        print("done")
                                    }
                                    callback(plantsList)
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
    }

    fun plantUpdatesFromJson(jsonArray: JSONArray) : List<PlantUpdate> {
        print("1111")
        var listOfPlantUpdates = mutableListOf<PlantUpdate>()
        for (i in 0 until jsonArray.length()) {

            var currentJsonUpdate = jsonArray.getJSONObject(i)
            var imageString = currentJsonUpdate["imageUrl"] as String
            imageString = imageString.replace("\\/", "/")
            var intHeight = currentJsonUpdate["height"] as Int
            var heightOfUpdate = intHeight.toDouble() / 10

            var update = PlantUpdate(heightOfUpdate, imageString, currentJsonUpdate["note"] as String, currentJsonUpdate["time"] as Int)

            listOfPlantUpdates.add(update)
        }
        print("33333")
        return listOfPlantUpdates
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun newPlant(plant: Plant, imgPath: String) {
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
                println(downloadUri.toString())
                uploadNewPlantToDb(plant, downloadUri.toString())
                println(downloadUri)
            } else {

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun newUpdate(imgPath: String, plant: Plant, plantUpdate: PlantUpdate, callback: (result: String) -> Unit) {
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
                plantUpdate.image = downloadUri.toString()
                println(downloadUri.toString())
                uploadUpdateToDb(plant, plantUpdate)
                println(downloadUri)

                callback(plantUpdate.image)
            } else {

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun uploadNewPlantToDb(plant: Plant, imageUrl: String) {
        val auth = Firebase.auth

        val user = auth.currentUser

        val url = URL(postUrl + "/new/" + user.uid)

        val jsonPlant = createJsonPlant(plant, imageUrl)
        val body = jsonPlant.toString()

        user.getIdToken(true)
            .addOnSuccessListener { result ->
                val idToken = result.token
                val bearerToken = idToken

                val thread = Thread(Runnable {
                    try {
                        val connection = url.openConnection()
                        connection.setRequestProperty("Bearer", bearerToken)

                        with(url.openConnection() as HttpURLConnection) {
                            requestMethod = "POST"  // optional default is GET
                            setRequestProperty("Content-Type", "application/json; charset=utf-8")
                            setRequestProperty("Authorization","Bearer "+ bearerToken)

                            val outputWriter = OutputStreamWriter(outputStream)
                            outputWriter.write(body)
                            outputWriter.flush()

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
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun uploadUpdateToDb(plant: Plant, update: PlantUpdate) {
        val auth = Firebase.auth

        val user = auth.currentUser

        val url = URL(postUrl + "/newUpdate/" + user.uid)

        val jsonUpdate = createJsonUpdate(plant.plantId, update)
        val body = jsonUpdate.toString()

        user.getIdToken(true)
            .addOnSuccessListener { result ->
                val idToken = result.token
                val bearerToken = idToken

                val thread = Thread(Runnable {
                    try {
                        val connection = url.openConnection()
                        connection.setRequestProperty("Bearer", bearerToken)

                        with(url.openConnection() as HttpURLConnection) {
                            requestMethod = "PUT"  // optional default is GET
                            setRequestProperty("Content-Type", "application/json; charset=utf-8")
                            setRequestProperty("Authorization","Bearer "+ bearerToken)

                            val outputWriter = OutputStreamWriter(outputStream)
                            outputWriter.write(body)
                            outputWriter.flush()

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
    }

    private fun createJsonPlant(plant: Plant, imageUrl: String): JSONObject {
        val jsonObject = JSONObject()

        val currentTime = System.currentTimeMillis() / 1000;
        print(currentTime)

        var heightAsInt = (plant.height * 10).toInt()

        jsonObject.put("title", plant.name)
        jsonObject.put("image", imageUrl)
        jsonObject.put("watering", plant.wateringFreq)
        jsonObject.put("temperature", plant.temperature)
        jsonObject.put("sunlight", plant.sunlight)
        jsonObject.put("note", plant.note)
        jsonObject.put("height", heightAsInt)
        jsonObject.put("time",  currentTime)

        return jsonObject
    }

    private fun createJsonUpdate(plantId: String, update: PlantUpdate): JSONObject {
        val jsonObject = JSONObject()

        val currentTime = System.currentTimeMillis() / 1000;
        print(currentTime)

        var heightAsInt = (update.height * 10).toInt()

        jsonObject.put("plantId", plantId)
        jsonObject.put("imageUrl", update.image)
        jsonObject.put("note", update.note)
        jsonObject.put("height", heightAsInt)
        jsonObject.put("time",  currentTime)

        return jsonObject
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

}
