package com.example.tasks.src.features.auth.presentation.view


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import  androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModernLoginSignUpToggle(selected: Boolean, onToggle: () -> Unit) {
    val selectorOffset by animateDpAsState(
        targetValue = if (selected) 0.dp else 160.dp, // Adjusted for better animation
        animationSpec = tween(300),
        label = "SelectorOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFFF3F4F6))
    ) {
        // Animated selector background
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(160.dp)
                .offset(x = selectorOffset)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Login button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        if (!selected) onToggle()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Login",
                    color = if (selected) Color.White else Color(0xFF6B7280),
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = 16.sp
                )
            }

            // Sign Up button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        if (selected) onToggle()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign Up",
                    color = if (!selected) Color.White else Color(0xFF6B7280),
                    fontWeight = if (!selected) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}