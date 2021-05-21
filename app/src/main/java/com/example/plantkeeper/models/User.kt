package com.example.plantkeeper.models

import org.json.JSONArray
import org.json.JSONObject

object User {

    operator fun invoke(userData: JSONObject): User {
        User.name = userData["name"] as String
        setFriendCountFromData(userData["friends"] as JSONArray)

        return this
    }

    var name: String = ""
    var friendCount = 0

    fun setFriendCountFromData(data: JSONArray) {
        friendCount = data.length()
    }
}