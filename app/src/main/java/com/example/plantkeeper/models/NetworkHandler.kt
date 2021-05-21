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
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class NetworkHandler {
    private val postUrl = "https://us-central1-plantkeeper-44769.cloudfunctions.net/post"
    private val userUrl = "https://us-central1-plantkeeper-44769.cloudfunctions.net/user"

    private val auth = Firebase.auth
    val user = auth.currentUser

    @RequiresApi(Build.VERSION_CODES.N)
    fun getFriendPosts(callback: (List<PlantUpdate>) -> Unit) {
        var updatesList = listOf<PlantUpdate>()
        val url = URL(postUrl + "/friendPosts/" + user.uid)

        user.getIdToken(true)
            .addOnSuccessListener { result ->
                val idToken = result.token
                val bearerToken = idToken

                val thread = Thread(Runnable {
                    try {
                        val connection = url.openConnection()
                        connection.setRequestProperty("Bearer", bearerToken)
                        with(url.openConnection() as HttpURLConnection) {
                            requestMethod = "GET"
                            setRequestProperty("Authorization","Bearer "+ bearerToken)

                            inputStream.bufferedReader().use {

                                it.lines().forEach { users ->
                                    val usersArray = JSONArray(users)
                                    for (i in 0 until usersArray.length()) {
                                        val user = usersArray[i] as JSONObject

                                        val plants = user["plants"] as JSONArray
                                        val plantsArray = plants

                                        for (i in 0 until plantsArray.length()) {
                                            val plantObject = plantsArray.getJSONObject(i)
                                            val updates = plantObject["updates"] as JSONArray

                                            val plantUpdates = plantUpdatesFromJson(updates, user["userName"] as String)

                                            updatesList = updatesList + plantUpdates

                                        }
                                    }
                                }
                            }
                            callback(updatesList)
                        }

                    }
                    catch (e:Exception) {
                        println(e)
                    }
                })
                thread.start()
            }
    }

    fun addFriend(email: String, callback: () -> Unit) {
        val url = URL(userUrl + "/addFriend/" + user.uid)

        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        var body = jsonObject.toString()

        user.getIdToken(true)
            .addOnSuccessListener { result ->
                val idToken = result.token
                val bearerToken = idToken

                val thread = Thread(Runnable {
                    try {
                        val connection = url.openConnection()
                        connection.setRequestProperty("Bearer", bearerToken)

                        with(url.openConnection() as HttpURLConnection) {
                            requestMethod = "POST"
                            setRequestProperty("Content-Type", "application/json; charset=utf-8")
                            setRequestProperty("Authorization","Bearer "+ bearerToken)

                            val outputWriter = OutputStreamWriter(outputStream)
                            outputWriter.write(body)
                            outputWriter.flush()
                            callback()

                            inputStream.bufferedReader().use {

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

    fun saveUserName(userName: String, callback: () -> Unit) {
        val url = URL(userUrl + "/userName/" + user.uid)

        var userEmail = user.email

        val jsonObject = JSONObject()
        jsonObject.put("userName", userName)
        jsonObject.put("email", userEmail)
        var body = jsonObject.toString()

        user.getIdToken(true)
            .addOnSuccessListener { result ->
                val idToken = result.token
                val bearerToken = idToken

                val thread = Thread(Runnable {
                    try {
                        val connection = url.openConnection()
                        connection.setRequestProperty("Bearer", bearerToken)

                        with(url.openConnection() as HttpURLConnection) {
                            requestMethod = "POST"
                            setRequestProperty("Content-Type", "application/json; charset=utf-8")
                            setRequestProperty("Authorization","Bearer "+ bearerToken)

                            val outputWriter = OutputStreamWriter(outputStream)
                            outputWriter.write(body)
                            outputWriter.flush()
                            callback()

                            inputStream.bufferedReader().use {

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
    fun getUserData(callback: (result: JSONObject) -> Unit) {
        val url = URL(userUrl + "/" + user.uid)

        user.getIdToken(true)
            .addOnSuccessListener { result ->
                val idToken = result.token
                val bearerToken = idToken

                val thread = Thread(Runnable {
                    try {
                        val connection = url.openConnection()
                        connection.setRequestProperty("Bearer", bearerToken)

                        with(url.openConnection() as HttpURLConnection) {
                            requestMethod = "GET"
                            setRequestProperty("Authorization", "Bearer " + bearerToken)

                            inputStream.bufferedReader().use {
                                var plantsList = mutableListOf<Plant>()
                                it.lines().forEach { line ->
                                    var jsonObject = JSONObject(line)
                                    callback(jsonObject)
                                }
                            }
                        }

                    } catch (e: Exception) {
                        println(e)
                    }
                })
                thread.start()
            }
    }

        @RequiresApi(Build.VERSION_CODES.N)
    fun getUserPosts(callback: (result: List<Plant>) -> Unit) {
        val url = URL(postUrl + "/" + user.uid)
        user.getIdToken(true)
            .addOnSuccessListener { result ->
                val idToken = result.token
                val bearerToken = idToken

                val thread = Thread(Runnable {
                    try {
                        val connection = url.openConnection()
                        connection.setRequestProperty("Bearer", bearerToken)
                        with(url.openConnection() as HttpURLConnection) {
                            requestMethod = "GET"
                            setRequestProperty("Authorization","Bearer "+ bearerToken)
                            inputStream.bufferedReader().use {
                                var plantsList = mutableListOf<Plant>()
                                it.lines().forEach { line ->
                                    var array = JSONArray(line)
                                    for (i in 0 until array.length()) {
                                        val jsonObject = array.getJSONObject(i)
                                        var imageString = jsonObject["imageUrl"] as String
                                        imageString = imageString.replace("\\/", "/")
                                        var height: Double = -10.0

                                        try {
                                            var intHeight = jsonObject["height"] as Int
                                            height = intHeight.toDouble() / 10

                                        } catch (e: Error) {

                                        }

                                        var plantUpdatesJson = jsonObject["updates"] as JSONArray
                                        var plantUpdates = plantUpdatesFromJson(plantUpdatesJson, User.name)
                                        var plantFromJson = plantFromJson(imageString, jsonObject, height, plantUpdates)

                                        plantsList.add(plantFromJson)

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
                uploadNewPlantToDb(plant, downloadUri.toString())
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
                uploadUpdateToDb(plant, plantUpdate)
                callback(plantUpdate.image)
            } else {

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun uploadNewPlantToDb(plant: Plant, imageUrl: String) {
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
                            requestMethod = "POST"
                            setRequestProperty("Content-Type", "application/json; charset=utf-8")
                            setRequestProperty("Authorization","Bearer "+ bearerToken)

                            val outputWriter = OutputStreamWriter(outputStream)
                            outputWriter.write(body)
                            outputWriter.flush()

                            inputStream.bufferedReader().use {
                                it.lines().forEach { line ->

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
        val url = URL(postUrl + "/newUpdate/" + user.uid)

        val jsonUpdate = createJsonUpdate(plant.plantId, update, plant.creationTime)
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
                            requestMethod = "PUT"
                            setRequestProperty("Content-Type", "application/json; charset=utf-8")
                            setRequestProperty("Authorization","Bearer "+ bearerToken)

                            val outputWriter = OutputStreamWriter(outputStream)
                            outputWriter.write(body)
                            outputWriter.flush()

                            inputStream.bufferedReader().use {
                                it.lines().forEach { line ->

                                }
                            }
                        }

                    }
                    catch (e:Exception) {
                    }
                })
                thread.start()
            }
    }

    private fun plantFromJson(
        imageString: String,
        jsonObject: JSONObject,
        height: Double,
        plantUpdates: List<PlantUpdate>
    ): Plant {
        return Plant(
            imageString,
            jsonObject["title"] as String,
            jsonObject["watering"] as Int,
            jsonObject["temperature"] as Int,
            jsonObject["sunlight"] as Int,
            jsonObject["note"] as String,
            height,
            jsonObject["id"] as String,
            plantUpdates,
            jsonObject["creationTime"] as Int)
    }

    fun plantUpdatesFromJson(jsonArray: JSONArray, userName: String) : List<PlantUpdate> {
        var listOfPlantUpdates = mutableListOf<PlantUpdate>()
        for (i in 0 until jsonArray.length()) {

            var currentJsonUpdate = jsonArray.getJSONObject(i)
            var imageString = currentJsonUpdate["imageUrl"] as String
            imageString = imageString.replace("\\/", "/")
            var intHeight = currentJsonUpdate["height"] as Int
            var heightOfUpdate = intHeight.toDouble() / 10

            var update = PlantUpdate(heightOfUpdate, imageString, currentJsonUpdate["note"] as String, currentJsonUpdate["daysOld"] as String, currentJsonUpdate["time"] as Int, userName)

            listOfPlantUpdates.add(update)
        }
        return listOfPlantUpdates
    }

    private fun createJsonPlant(plant: Plant, imageUrl: String): JSONObject {
        val jsonObject = JSONObject()
        val currentTime = System.currentTimeMillis() / 1000;
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

    private fun createJsonUpdate(plantId: String, update: PlantUpdate, plantCreationTime: Int): JSONObject {
        val jsonObject = JSONObject()
        val currentTime = System.currentTimeMillis() / 1000;
        var daysBetweenUpdateAndCreation = getDateDifference(plantCreationTime.toLong(), currentTime.toLong())

        var heightAsInt = (update.height * 10).toInt()

        jsonObject.put("plantId", plantId)
        jsonObject.put("imageUrl", update.image)
        jsonObject.put("note", update.note)
        jsonObject.put("height", heightAsInt)
        jsonObject.put("daysOld", update.daysOld)
        jsonObject.put("time",  update.timeCreated)

        return jsonObject
    }

    fun getDateDifference(startDate: Long, endDate: Long): Int {
        val different = (endDate * 1000) - (startDate * 1000)
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        return elapsedDays.toInt()
    }

}
