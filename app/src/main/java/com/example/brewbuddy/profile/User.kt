package com.example.brewbuddy.profile

import com.example.brewbuddy.R

class User {

    var firstName: String = "";
    var lastName: String = "";
    private var username: String = "";
    private var profilePicture: Int = R.drawable.profile_picture
    constructor(username: String) {
        this.username = username
    }

    fun getUsername(): String {
        return username
    }

    fun getAvatar(): Int {
        return profilePicture;
    }
}