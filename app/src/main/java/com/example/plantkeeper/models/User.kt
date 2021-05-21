package com.example.plantkeeper.models

object User {

    operator fun invoke(name: String): User {
        User.name = name

        return this
    }

    var name: String = ""
}