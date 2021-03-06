package com.example.plantkeeper.models

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.plantkeeper.R
import com.example.plantkeeper.activities.MainActivity
import com.example.plantkeeper.fragments.HomeFragment
import org.json.JSONArray
import org.json.JSONObject

object User {
    var networkHandler = NetworkHandler()
    var name: String = ""
    var friendCount = 0

    @RequiresApi(Build.VERSION_CODES.N)
    operator fun invoke(isNewUser: Boolean, name: String? = null, callback: () -> Unit): User {
        if (isNewUser && name != null) {
            networkHandler.saveUserName(name) {
                User.name = name
                callback()
            }
        } else {
            var userData = getUserData { userData ->
                User.name = userData["name"] as String
                setFriendCountFromData(userData["friends"] as JSONArray)
                callback()
            }
        }

        return this
    }

    fun setFriendCountFromData(data: JSONArray) {
        friendCount = data.length()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getUserData(callback: (JSONObject) -> Unit) {
        networkHandler.getUserData {userData ->
            callback(userData)
        }
    }

    fun addNewFriend(emailToAdd: String, callback: () -> Unit) {
        networkHandler.addFriend(emailToAdd) {
            this.friendCount = User.friendCount + 1
            callback()
        }
    }
}