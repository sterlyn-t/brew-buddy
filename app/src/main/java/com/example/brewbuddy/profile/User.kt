package com.example.brewbuddy.profile

import android.util.Log

class User {

    var firstName: String = "";
    var lastName: String = "";
    var username: String = "";
    constructor(username: String) {
        this.username = username
    }
}