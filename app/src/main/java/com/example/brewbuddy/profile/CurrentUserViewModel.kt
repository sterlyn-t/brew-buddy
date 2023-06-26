package com.example.brewbuddy.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.*
import com.example.brewbuddy.profile.User
import kotlinx.coroutines.launch


// global state view model to get currently logged in user.
// if this is null, then we are looking at the login screen
class CurrentUserViewModel : ViewModel() {
    val currentUser =  MutableLiveData<User?>()

    fun setUser(user: User?) {
        currentUser.postValue(user);
    }

    fun getUser(): User {
        return currentUser.value!!
    }

    private fun validateUser(username: String, password: String): Boolean {
        return username == "test" && password == "test"
    }

    fun loginUser(username: String, password: String): Boolean {
        if(validateUser(username, password)) {
            val user = User(username)
            setUser(user)
            return true;
        }
        return false;
    }

    fun registerUser(username: String, password: String): Boolean {
        if(username != "test") {
            val user = User(username);
            setUser(user);
            return true;
        }
        return false;
    }
}