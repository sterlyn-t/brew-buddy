package com.example.brewbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brewbuddy.ui.components.CustomCard
import com.example.brewbuddy.ui.theme.BrewBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrewBuddyTheme {
                // A surface container using the 'background' color from the theme
               // Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Box() {
                    Row() {
                        Box(
                            modifier = Modifier.size(width = 200.dp, height = 260.dp).padding(15.dp)
                        ) {
                            CustomCard()
                        }
                        Box(
                            modifier = Modifier.size(width = 200.dp, height = 260.dp).padding(15.dp)
                        ) {
                            CustomCard()
                        }
                    }
                }
               // }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CardPreview() {
    BrewBuddyTheme {
        CustomCard()
    }
}
