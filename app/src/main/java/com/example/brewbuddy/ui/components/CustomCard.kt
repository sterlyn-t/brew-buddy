package com.example.brewbuddy.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.brewbuddy.R

// TODO: Use Glide for async image loading
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CustomCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth()
            .clickable { },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        )
    )
    {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            // TODO: Pass icon as parameter
            Image(
                painter = painterResource(id = R.drawable.baseline_store_24),
                contentDescription = "Test",
                modifier = Modifier
                    .size(35.dp)
                    .padding(horizontal = 0.dp, vertical = 2.dp)
            )
            Box( modifier = Modifier.padding(vertical = 10.dp)) {
                Column() {
                    Text(
                        text = "Testing Long Card Title",
                        modifier = Modifier.padding(start = 1.dp, end = 1.dp, top = 15.dp, bottom = 0.dp),
                        fontSize = 22.sp,
                        color = Color.White
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 0.dp, end = 0.dp, top = 10.dp, bottom= 0.dp)
                    ) {
                        Text(
                            text = "User Name",
                            modifier = Modifier
                                .padding(start = 0.dp, end = 5.dp, top = 0.dp, bottom = 0.dp),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Box(
                            modifier = Modifier
                                .size(28.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(SolidColor(Color.White))
                            }
                            // TODO: Replace with GlideImage
                            Box(modifier = Modifier.size(22.dp)) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawCircle(SolidColor(Color.Black))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
