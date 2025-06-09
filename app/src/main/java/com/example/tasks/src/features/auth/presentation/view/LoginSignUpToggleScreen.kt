package com.example.tasks.src.features.auth.presentation.view


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import  androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp


@Composable
fun LoginSignUpToggle(selected: Boolean, onToggle: (Boolean) -> Unit) {
    val selectorWidthFraction = 0.5f


    val selectorOffset by animateDpAsState(
        targetValue = if (selected) 0.dp else (selectorWidthFraction * 300).dp,
        label = "SelectorOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp)
            .height(53.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.LightGray)

    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(selectorWidthFraction)
                .offset(x = selectorOffset)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFD9FF1D))
        )

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onToggle(true) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Login",
                    color = if (selected) Color.Black else Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()

                    .clickable { onToggle(false) },

                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign Up",
                    color = if (!selected) Color.Black else Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}