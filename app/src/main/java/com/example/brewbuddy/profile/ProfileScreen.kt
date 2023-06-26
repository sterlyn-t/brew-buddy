package com.example.brewbuddy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brewbuddy.profile.CurrentUserViewModel

@Composable
fun ProfileScreen(
) {
    val currentUserViewModel: CurrentUserViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    val user = currentUserViewModel.getUser()
    val username = user!!.username
    Surface(modifier = Modifier.fillMaxSize()) {
        Text("Welcome, $username")
    }

}