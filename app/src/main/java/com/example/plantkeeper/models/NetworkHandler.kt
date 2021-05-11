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
    val postUrl = "https://us-central1-plantkeeper-44769.cloudfunctions.net/post"
    val userUrl = "https://us-central1-plantkeeper-44769.cloudfunctions.net/user"

    @RequiresApi(Build.VERSION_CODES.N)
    fun getFriendPosts(callback: (List<PlantUpdate>) -> Unit) {
        var updatesList = listOf<PlantUpdate>()

        val auth = Firebase.auth
        val user = auth.currentUser

        val url = URL(postUrl + "/friendPosts/" + user.uid)

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
                            println(bearerToken)
                            println("111")

                            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

                            inputStream.bufferedReader().use {

                                it.lines().forEach { users ->
                                    var usersArray = JSONArray(users)

                                    for (i in 0 until usersArray.length()) {
                                        var user = usersArray[i] as JSONObject

                                        var plants = user["plants"] as JSONArray
                                        var plantsArray = plants
                                        //Plant(val image: String, val name: String, var wateringFreq: Int, var temperature: Int, var sunlight: Int, var note: String) {
                                        for (i in 0 until plantsArray.length()) {
                                            println("03")
                                            var plantObject = plantsArray.getJSONObject(i)
                                            println(plantObject)
                                            var updates = plantObject["updates"] as JSONArray

                                            var plantUpdates = plantUpdatesFromJson(updates)

                                            updatesList = updatesList + plantUpdates

                                        }
                                    }
                                }

                            }
                            println(updatesList)
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
        val auth = Firebase.auth
        val user = auth.currentUser

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
                            requestMethod = "POST"  // optional default is GET
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
        val auth = Firebase.auth
        val user = auth.currentUser

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
                            requestMethod = "POST"  // optional default is GET
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
                            println(bearerToken)
                            println("111")

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
                                            plantUpdates,
                                            jsonObject["creationTime"] as Int)

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

    private fun createJsonUpdate(plantId: String, update: PlantUpdate, plantCreationTime: Int): JSONObject {
        val jsonObject = JSONObject()

        val currentTime = System.currentTimeMillis() / 1000;

        var daysBetweenUpdateAndCreation = getDateDifference(plantCreationTime.toLong(), currentTime.toLong())
        print(currentTime)

        var heightAsInt = (update.height * 10).toInt()

        jsonObject.put("plantId", plantId)
        jsonObject.put("imageUrl", update.image)
        jsonObject.put("note", update.note)
        jsonObject.put("height", heightAsInt)
        jsonObject.put("time",  daysBetweenUpdateAndCreation)

        return jsonObject
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getDateDifference(startDate: Long, endDate: Long): Int {

        //milliseconds
        val different = (endDate * 1000) - (startDate * 1000)
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        return elapsedDays.toInt()
    }

}
