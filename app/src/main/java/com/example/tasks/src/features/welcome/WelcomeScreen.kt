package com.example.tasks.src.features.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasks.R

@Composable
fun WelcomeScreen(navigateToLogin: () -> Unit){
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    )


    {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.8f)
            ,
            painter = painterResource(id =R.drawable.font_app ),
            contentDescription = "Sticky Notes Background",
            contentScale = ContentScale.Crop

        )

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Sticky Notes Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(top = 250.dp)
                .size(250.dp),


            )

        Button(
            modifier = Modifier
                .padding(top = 560.dp)
                .fillMaxWidth(5/8f)
                .height(50.dp)
            ,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD9FF1D)
            ),
            onClick = {navigateToLogin()}
        ) {

            Text(
                color = Color.Black,
                text = "Login",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}