package com.example.brewbuddy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.brewbuddy.R

val InterFont = FontFamily(
        Font(R.font.inter_regular),
        Font(R.font.inter_regular_italic, style = FontStyle.Italic),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_bold_italic, FontWeight.Bold, style = FontStyle.Italic)
)


// Set of Material typography styles to start with
val Typography = Typography(
        bodyLarge = TextStyle(
                fontFamily = InterFont,
                fontSize = 16.sp,
                letterSpacing = 0.5.sp
        ),
        titleLarge = TextStyle(
                fontFamily = InterFont,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
                fontFamily = InterFont,
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp,
                letterSpacing = 0.sp
        ),
        titleSmall = TextStyle(
                fontFamily = InterFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 0.sp
        ),
        labelSmall = TextStyle(
                fontFamily = InterFont,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
        )

)