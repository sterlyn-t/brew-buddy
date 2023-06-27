package com.example.brewbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brewbuddy.ui.theme.BrewBuddyTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrewBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(Modifier.fillMaxSize()) {
                        Greeting("BrewBuddy")
                        NavBar()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color = Color.Gray) {
        Text(
            text = "Hello from $name!",
            modifier = modifier.padding(24.dp)
        )
    }
}

@Composable
    fun NavBar() {
    Row(Modifier.background(Color.Gray).fillMaxWidth().padding(16.dp)) {
        Text("Home", Modifier.padding(8.dp).clickable { /* Handle click event */ })
        Text("Profile", Modifier.padding(8.dp).clickable { /* Handle click event */ })
        Text("Settings", Modifier.padding(8.dp).clickable { /* Handle click event */ })
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BrewBuddyTheme {
        Greeting("BrewBuddy")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BrewBuddyTheme {
        Greeting("BrewBuddy")
    }
}